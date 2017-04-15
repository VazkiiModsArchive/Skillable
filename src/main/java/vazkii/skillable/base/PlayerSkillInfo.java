package vazkii.skillable.base;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import vazkii.skillable.skill.Skill;
import vazkii.skillable.skill.Skills;
import vazkii.skillable.skill.base.Unlockable;

public class PlayerSkillInfo {

	private static final String TAG_LEVEL = "level";
	private static final String TAG_SKILL_POINTS = "skillPoints";
	private static final String TAG_UNLOCKABLES = "unlockables";
	
	public static final int MAX_LEVEL = 32;
	
	public final Skill skill;
	
	private int level;
	private int skillPoints;
	private List<Unlockable> unlockables = new ArrayList();
	
	public PlayerSkillInfo(Skill skill) {
		this.skill = skill;
		level = 1;
		respec();
	}
	
	public void loadFromNBT(NBTTagCompound cmp) {
		level = cmp.getInteger(TAG_LEVEL);
		skillPoints = cmp.getInteger(TAG_SKILL_POINTS);
		
		unlockables.clear();
		NBTTagCompound unlockablesCmp = cmp.getCompoundTag(TAG_UNLOCKABLES);
		for(String s : unlockablesCmp.getKeySet())
			if(Skills.ALL_UNLOCKABLES.containsKey(s))
				unlockables.add(Skills.ALL_UNLOCKABLES.get(s));
	}
	
	public void saveToNBT(NBTTagCompound cmp) {
		cmp.setInteger(TAG_LEVEL, level);
		cmp.setInteger(TAG_SKILL_POINTS, skillPoints);
		
		NBTTagCompound unlockablesCmp = new NBTTagCompound();
		for(Unlockable u : unlockables)
			unlockablesCmp.setBoolean(u.getKey(), true);
		cmp.setTag(TAG_UNLOCKABLES, unlockablesCmp);
	}
	
	public int getLevel() {
		if(level <= 0)
			level = 1;
		if(level > MAX_LEVEL)
			level = MAX_LEVEL;
			
		return level;
	}
	
	public int getRank() {
		return level / (MAX_LEVEL / 4);
	}
	
	public int getSkillPoints() {
		return skillPoints;
	}
	
	public boolean isCapped() {
		return level == MAX_LEVEL;
	}
	
	public int getLevelUpCost() {
		return ConfigHandler.baseXPCost + level * ConfigHandler.xpIncrease;
	}
	
	public boolean isUnlocked(Unlockable u) {
		return unlockables.contains(u);
	}
	
	public void levelUp() {
		level++;
		if(level % ConfigHandler.skillPointInterval == 0)
			skillPoints++;
	}
	
	public void unlock(Unlockable u) {
		skillPoints -= u.cost;
		unlockables.add(u);
	}
	
	public void respec() {
		unlockables.clear();
		skillPoints = level / ConfigHandler.skillPointInterval;
	}
	
}
