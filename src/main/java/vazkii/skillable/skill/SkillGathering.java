package vazkii.skillable.skill;

import net.minecraft.init.Blocks;
import vazkii.skillable.skill.gathering.TraitDropGuarantee;
import vazkii.skillable.skill.gathering.TraitLuckyFisherman;

public class SkillGathering extends Skill {

	public SkillGathering() {
		super("gathering", 1, Blocks.LOG);
	}

	@Override
	public void initUnlockables() {
		addUnlockable(new TraitLuckyFisherman());
		addUnlockable(new TraitDropGuarantee());
	}
	
}
