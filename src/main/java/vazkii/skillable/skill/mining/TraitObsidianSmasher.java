package vazkii.skillable.skill.mining;

import vazkii.skillable.skill.Skills;
import vazkii.skillable.skill.base.Trait;

public class TraitObsidianSmasher extends Trait {

	public TraitObsidianSmasher() {
		super("obsidian_smasher", 1, 2, 4);
		addRequirement(Skills.mining, 16);
	}

}
