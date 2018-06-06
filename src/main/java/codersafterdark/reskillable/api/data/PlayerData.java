package codersafterdark.reskillable.api.data;

import codersafterdark.reskillable.api.ReskillableAPI;
import codersafterdark.reskillable.api.ReskillableRegistries;
import codersafterdark.reskillable.api.requirement.Requirement;
import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.api.unlockable.Ability;
import codersafterdark.reskillable.api.unlockable.IAbilityEventHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.function.Consumer;

public class PlayerData {
    private static final String TAG_SKILLS_CMP = "SkillLevels";
    private final boolean client;
    public WeakReference<EntityPlayer> playerWR;
    private Map<Skill, PlayerSkillInfo> skillInfo = new HashMap<>();
    private Map<Requirement, Boolean> requirementCache = new HashMap<>();

    public PlayerData(EntityPlayer player) {
        playerWR = new WeakReference<>(player);
        client = player.getEntityWorld().isRemote;

        ReskillableRegistries.SKILLS.getValuesCollection().forEach(s -> skillInfo.put(s, new PlayerSkillInfo(s)));

        load();
    }

    public PlayerSkillInfo getSkillInfo(Skill s) {
        return skillInfo.get(s);
    }

    public Collection<PlayerSkillInfo> getAllSkillInfo() {
        return skillInfo.values();
    }

    public boolean hasAnyAbilities() {
        return !getAllAbilities().isEmpty();
    }

    public Set<Ability> getAllAbilities() {
        Set<Ability> set = new TreeSet<>();
        skillInfo.values().forEach(info -> info.addAbilities(set));
        return set;
    }

    //TODO Cont: This will allow nested requirements and tooltips (if the code gets properly updated) to store the value
    //TODO Cont: The value will still be valid so it can get the cached boolean because all parent checks get called before a new call to matchStats
    public boolean matchStats(RequirementHolder holder) {
        //TODO: Improve on the caching method so that it does not have to always reset the cache when a new requirement holder is being checked.
        //TODO Cont: This would also require a better way of deciding when something should be rechecked and how long to store the data for
        resetRequirementCache(); //Clears the previous cache
        EntityPlayer entityPlayer = playerWR.get();
        if (entityPlayer != null) {
            for (Requirement requirement : holder.getRequirements()) {
                //TODO figure out how to cache if it has been achieved and see if it has to be updated
                //TODO: Have a start cache and an "end" cache that removes it from being cached
                if (!requirementAchieved(requirement)) {
                    return false;
                }
            }
        }
        return true;
    }

    //TODO: Use this in the tooltip method of requirements, and also in the sub requirement parts of logic requirements
    public boolean requirementAchieved(Requirement requirement) {
        EntityPlayer entityPlayer = playerWR.get();
        if (entityPlayer != null) {
            if (requirementCache.containsKey(requirement)) {
                return requirementCache.get(requirement);
            }
            boolean achieved = requirement.achievedByPlayer(entityPlayer);
            requirementCache.put(requirement, achieved);
            return achieved;
        }
        return false;
    }

    //TODO: Does requirementCache get cleared often enough? For example for tooltips will it need this to be rechecked when stats/dimensions etc change
    //TODO Cont: Potentially make this get called in specific circumstances when things may not update properly.
    //TODO Cont: This will be needed if we want auto unlockables to be efficient
    //TODO: Make a way to "invalidate" certain requirement types. It may not be as efficient as manually removing same skill types instead of all skill requirement caches
    //TODO Cont: but it will probably work better. Logic Requirements probably should always be invalidated
    public void resetRequirementCache() {
        requirementCache.clear();
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
            ReskillableAPI.getInstance().syncPlayerData(player, this);
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
        forEachEventHandler(h -> h.onPlayerTick(event));
    }

    public void blockDrops(HarvestDropsEvent event) {
        forEachEventHandler(h -> h.onBlockDrops(event));
    }

    public void mobDrops(LivingDropsEvent event) {
        forEachEventHandler(h -> h.onMobDrops(event));
    }

    public void breakSpeed(BreakSpeed event) {
        forEachEventHandler(h -> h.getBreakSpeed(event));
    }

    public void attackMob(LivingHurtEvent event) {
        forEachEventHandler(h -> h.onAttackMob(event));
    }

    public void hurt(LivingHurtEvent event) {
        forEachEventHandler(h -> h.onHurt(event));
    }

    public void rightClickBlock(RightClickBlock event) {
        forEachEventHandler(h -> h.onRightClickBlock(event));
    }

    public void enderTeleport(EnderTeleportEvent event) {
        forEachEventHandler(h -> h.onEnderTeleport(event));
    }

    public void killMob(LivingDeathEvent event) {
        forEachEventHandler(h -> h.onKillMob(event));
    }

    public void forEachEventHandler(Consumer<IAbilityEventHandler> consumer) {
        skillInfo.values().forEach(info -> info.forEachEventHandler(consumer));
    }
}