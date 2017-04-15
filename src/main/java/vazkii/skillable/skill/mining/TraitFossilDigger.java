package vazkii.skillable.skill.mining;

import vazkii.skillable.skill.Skills;
import vazkii.skillable.skill.base.Trait;

public class TraitFossilDigger extends Trait {

	public TraitFossilDigger() {
		super("fossil_digger", 2, 1, 2);
		addRequirement(Skills.mining, 6);
	}

}
