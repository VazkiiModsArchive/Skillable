package vazkii.skillable.skill;

import net.minecraft.init.Blocks;
import vazkii.skillable.skill.agility.TraitRoadwalk;
import vazkii.skillable.skill.agility.TraitSidestep;

public class SkillAgility extends Skill {

	public SkillAgility() {
		super("agility", 6, Blocks.GRAVEL);
	}

	@Override
	public void initUnlockables() {
		addUnlockable(new TraitRoadwalk());
		addUnlockable(new TraitSidestep());
	}
	
}
