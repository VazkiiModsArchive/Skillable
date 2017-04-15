package vazkii.skillable.base;

import net.minecraft.nbt.NBTTagCompound;
import vazkii.skillable.skill.Skill;

public class PlayerSkillInfo {

	private static final String TAG_LEVEL = "level";
	private static final String TAG_RANK = "rank";
	
	public static final int MAX_LEVEL = 32;
	
	public final Skill skill;
	
	int level, rank;
	
	public PlayerSkillInfo(Skill skill) {
		this.skill = skill;
	}
	
	public void loadFromNBT(NBTTagCompound cmp) {
		level = cmp.getInteger(TAG_LEVEL);
		rank = cmp.getInteger(TAG_RANK);
	}
	
	public void saveToNBT(NBTTagCompound cmp) {
		cmp.setInteger(TAG_LEVEL, level);
		cmp.setInteger(TAG_RANK, rank);
	}
	
	public int getLevel() {
		if(level <= 0)
			level = 1;
			
		return level;
	}
	
	public int getRank() {
		return rank;
	}
	
	public int getMaxLevel() {
		return (rank + 1) * (MAX_LEVEL / 4);
	}
	
	public boolean isCapped() {
		return level == MAX_LEVEL;
	}
	
	public void rankUp() {
		rank++;
	}
	
	public void levelUp() {
		level++;
	}
	
}
