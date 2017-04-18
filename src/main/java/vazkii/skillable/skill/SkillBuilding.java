package vazkii.skillable.skill;

import net.minecraft.init.Blocks;
import vazkii.skillable.skill.building.TraitChorusTransmutation;

public class SkillBuilding extends Skill {

	public SkillBuilding() {
		super("building", 4, Blocks.BRICK_BLOCK);
	}

	@Override
	public void initUnlockables() {
		addUnlockable(new TraitChorusTransmutation());
	}
	
}
