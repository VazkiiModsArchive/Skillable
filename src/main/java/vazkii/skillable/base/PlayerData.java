package vazkii.skillable.base;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import vazkii.arl.network.NetworkHandler;
import vazkii.skillable.network.MessageDataSync;
import vazkii.skillable.skill.Skill;
import vazkii.skillable.skill.Skills;
import vazkii.skillable.skill.base.Ability;
import vazkii.skillable.skill.base.IAbilityEventHandler;

public class PlayerData {

	private static final String TAG_SKILLS_CMP = "SkillLevels";

	public WeakReference<EntityPlayer> playerWR;
	private final boolean client;

	private HashMap<Skill, PlayerSkillInfo> skillInfo = new HashMap();

	public PlayerData(EntityPlayer player) {
		playerWR = new WeakReference(player);
		client = player.getEntityWorld().isRemote;

		for(Skill s : Skills.ALL_SKILLS.values())
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
		for(PlayerSkillInfo info : skillInfo.values())
			info.addAbilities(set);
		
		return set;
	}
	
	public boolean matchStats(Map<Skill, Integer> stats) {
		for(Skill s : stats.keySet()) {
			PlayerSkillInfo info = getSkillInfo(s);
			if(info.getLevel() < stats.get(s))
				return false;
		}
			
		return true;
	}
	
	public void load() {
		if(!client) {
			EntityPlayer player = playerWR.get();

			if(player != null) {
				NBTTagCompound cmp = PlayerDataHandler.getDataCompoundForPlayer(player);
				loadFromNBT(cmp);
			}
		}
	}

	public void save() {
		if(!client) {
			EntityPlayer player = playerWR.get();

			if(player != null) {
				NBTTagCompound cmp = PlayerDataHandler.getDataCompoundForPlayer(player);
				saveToNBT(cmp);
			}
		}
	}

	public void sync() {
		if(!client) {
			EntityPlayer player = playerWR.get();

			if(player != null && player instanceof EntityPlayerMP) {
				MessageDataSync message = new MessageDataSync(this);
				NetworkHandler.INSTANCE.sendTo(message, (EntityPlayerMP) player);
			}
		}
	}
	
	public void saveAndSync() {
		save();
		sync();
	}

	public void loadFromNBT(NBTTagCompound cmp) {
		NBTTagCompound skillsCmp = cmp.getCompoundTag(TAG_SKILLS_CMP);
		for(PlayerSkillInfo info : skillInfo.values()) {
			String key = info.skill.getKey();
			if(skillsCmp.hasKey(key)) {
				NBTTagCompound infoCmp = skillsCmp.getCompoundTag(key);
				info.loadFromNBT(infoCmp);
			}
		}
	}

	public void saveToNBT(NBTTagCompound cmp) {
		NBTTagCompound skillsCmp = new NBTTagCompound();
		
		for(PlayerSkillInfo info : skillInfo.values()) {
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
	
	public void forEachEventHandler(Consumer<IAbilityEventHandler> consumer) {
		skillInfo.values().forEach((info) -> info.forEachEventHandler(consumer));
	}

}