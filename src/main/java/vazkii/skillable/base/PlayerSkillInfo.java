package vazkii.skillable.base;

import net.minecraft.nbt.NBTTagCompound;
import vazkii.skillable.skill.Skill;

public class PlayerSkillInfo {

	private static final String TAG_LEVEL = "level";
	
	public static final int MAX_LEVEL = 32;
	
	public final Skill skill;
	
	int level;
	
	public PlayerSkillInfo(Skill skill) {
		this.skill = skill;
	}
	
	public void loadFromNBT(NBTTagCompound cmp) {
		level = cmp.getInteger(TAG_LEVEL);
	}
	
	public void saveToNBT(NBTTagCompound cmp) {
		cmp.setInteger(TAG_LEVEL, level);
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
	
	public boolean isCapped() {
		return level == MAX_LEVEL;
	}
	
	public int getLevelUpCost() {
		return ConfigHandler.baseXPCost + level * ConfigHandler.xpIncrease;
	}
	
	public void levelUp() {
		level++;
	}
	
}
