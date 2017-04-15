package vazkii.skillable.base;

import net.minecraft.nbt.NBTTagCompound;
import vazkii.skillable.skill.Skill;

public class PlayerSkillInfo {

	private static final String TAG_LEVEL = "level";
	private static final String TAG_RANK = "rank";
	
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
		return level;
	}
	
	public int getRank() {
		return rank;
	}
	
	public int getMaxLevel() {
		return (rank + 1) * 8;
	}
	
	public void rankUp() {
		rank++;
	}
	
	public void levelUp() {
		level++;
	}
	
}
