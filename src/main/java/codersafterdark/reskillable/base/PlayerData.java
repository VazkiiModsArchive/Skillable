package codersafterdark.reskillable.base;

import codersafterdark.reskillable.network.*;
import codersafterdark.reskillable.skill.Skill;
import codersafterdark.reskillable.skill.Skills;
import codersafterdark.reskillable.skill.base.Ability;
import codersafterdark.reskillable.skill.base.IAbilityEventHandler;
import mcp.mobius.waila.handlers.NetworkHandler;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;

public class PlayerData {

    private static final String TAG_SKILLS_CMP = "SkillLevels";
    private final boolean client;
    public WeakReference<EntityPlayer> playerWR;
    private HashMap<Skill, PlayerSkillInfo> skillInfo = new HashMap();

    public PlayerData(EntityPlayer player) {
        playerWR = new WeakReference(player);
        client = player.getEntityWorld().isRemote;

        for (Skill s : Skills.SKILLS.values())
            skillInfo.put(s, new PlayerSkillInfo(s));

        load();
    }

    public PlayerSkillInfo getSkillInfo(Skill s) {
        return skillInfo.get(s);
    }

    public boolean hasAnyAbilities() {
        return !getAllAbilities().isEmpty();
    }

    public Set<Ability> getAllAbilities() {
        Set<Ability> set = new TreeSet();
        for (PlayerSkillInfo info : skillInfo.values())
            info.addAbilities(set);

        return set;
    }

    public boolean matchStats(RequirementHolder holder) {
        EntityPlayer player = playerWR.get();
        if (player == null)
            return false;

        for (Skill s : holder.skillLevels.keySet()) {
            PlayerSkillInfo info = getSkillInfo(s);
            if (info.getLevel() < holder.skillLevels.get(s))
                return false;
        }

        if (player instanceof EntityPlayerMP) {
            EntityPlayerMP mp = (EntityPlayerMP) player;
            AdvancementManager manager = ((WorldServer) mp.world).getAdvancementManager();

            for (ResourceLocation res : holder.advancements) {
                Advancement adv = manager.getAdvancement(res);
                if (adv != null) {
                    AdvancementProgress progress = mp.getAdvancements().getProgress(adv);
                    if (!progress.isDone())
                        return false;
                }
            }
        }

        return true;
    }

    public void load() {
        if (!client) {
            EntityPlayer player = playerWR.get();

            if (player != null) {
                NBTTagCompound cmp = PlayerDataHandler.getDataCompoundForPlayer(player);
                loadFromNBT(cmp);
            }
        }
    }

    public void save() {
        if (!client) {
            EntityPlayer player = playerWR.get();

            if (player != null) {
                NBTTagCompound cmp = PlayerDataHandler.getDataCompoundForPlayer(player);
                saveToNBT(cmp);
            }
        }
    }

    public void sync() {
        if (!client) {
            EntityPlayer player = playerWR.get();

            if (player != null && player instanceof EntityPlayerMP) {
                MessageDataSync message = new MessageDataSync(this);
                PacketHandler.INSTANCE.sendTo(message, (EntityPlayerMP) player);
            }
        }
    }

    public void saveAndSync() {
        save();
        sync();
    }

    public void loadFromNBT(NBTTagCompound cmp) {
        NBTTagCompound skillsCmp = cmp.getCompoundTag(TAG_SKILLS_CMP);
        for (PlayerSkillInfo info : skillInfo.values()) {
            String key = info.skill.getKey();
            if (skillsCmp.hasKey(key)) {
                NBTTagCompound infoCmp = skillsCmp.getCompoundTag(key);
                info.loadFromNBT(infoCmp);
            }
        }
    }

    public void saveToNBT(NBTTagCompound cmp) {
        NBTTagCompound skillsCmp = new NBTTagCompound();

        for (PlayerSkillInfo info : skillInfo.values()) {
            String key = info.skill.getKey();
            NBTTagCompound infoCmp = new NBTTagCompound();
            info.saveToNBT(infoCmp);
            skillsCmp.setTag(key, infoCmp);
        }

        cmp.setTag(TAG_SKILLS_CMP, skillsCmp);
    }

    // Event Handlers

    public void tickPlayer(PlayerTickEvent event) {
        forEachEventHandler((h) -> h.onPlayerTick(event));
    }

    public void blockDrops(HarvestDropsEvent event) {
        forEachEventHandler((h) -> h.onBlockDrops(event));
    }

    public void mobDrops(LivingDropsEvent event) {
        forEachEventHandler((h) -> h.onMobDrops(event));
    }

    public void breakSpeed(BreakSpeed event) {
        forEachEventHandler((h) -> h.getBreakSpeed(event));
    }

    public void attackMob(LivingHurtEvent event) {
        forEachEventHandler((h) -> h.onAttackMob(event));
    }

    public void hurt(LivingHurtEvent event) {
        forEachEventHandler((h) -> h.onHurt(event));
    }

    public void rightClickBlock(RightClickBlock event) {
        forEachEventHandler((h) -> h.onRightClickBlock(event));
    }

    public void enderTeleport(EnderTeleportEvent event) {
        forEachEventHandler((h) -> h.onEnderTeleport(event));
    }

    public void killMob(LivingDeathEvent event) {
        forEachEventHandler((h) -> h.onKillMob(event));
    }

    public void forEachEventHandler(Consumer<IAbilityEventHandler> consumer) {
        skillInfo.values().forEach((info) -> info.forEachEventHandler(consumer));
    }

}