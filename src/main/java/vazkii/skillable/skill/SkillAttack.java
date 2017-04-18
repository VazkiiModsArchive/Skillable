package vazkii.skillable.skill;

import net.minecraft.init.Blocks;
import vazkii.skillable.skill.attack.TraitNeutralissse;

public class SkillAttack extends Skill {

	public SkillAttack() {
		super("attack", 2, Blocks.STONEBRICK);
	}

	@Override
	public void initUnlockables() {
		addUnlockable(new TraitNeutralissse());
	}
	
}
