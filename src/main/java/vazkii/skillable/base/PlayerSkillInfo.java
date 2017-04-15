package vazkii.skillable.base;

import net.minecraft.nbt.NBTTagCompound;
import vazkii.skillable.skill.Skill;

public class PlayerSkillInfo {

	private static final String TAG_LEVEL = "level";
	private static final String TAG_SKILL_POINTS = "skillPoints";

	public static final int MAX_LEVEL = 32;
	
	public final Skill skill;
	
	private int level;
	private int skillPoints;
	
	public PlayerSkillInfo(Skill skill) {
		this.skill = skill;
	}
	
	public void loadFromNBT(NBTTagCompound cmp) {
		level = cmp.getInteger(TAG_LEVEL);
		skillPoints = cmp.getInteger(TAG_SKILL_POINTS);
	}
	
	public void saveToNBT(NBTTagCompound cmp) {
		cmp.setInteger(TAG_LEVEL, level);
		cmp.setInteger(TAG_SKILL_POINTS, skillPoints);
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
	
	public void levelUp() {
		level++;
		if(level % ConfigHandler.skillPointInterval == 0)
			skillPoints++;
	}
	
}
