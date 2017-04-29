package vazkii.skillable.skill;

import net.minecraft.init.Blocks;
import vazkii.skillable.skill.defense.TraitEffectTwist;
import vazkii.skillable.skill.defense.TraitUndershirt;

public class SkillDefense extends Skill {

	public SkillDefense() {
		super("defense", 3, Blocks.QUARTZ_BLOCK);
	}

	@Override
	public void initUnlockables() {
		addUnlockable(new TraitUndershirt());
		addUnlockable(new TraitEffectTwist());
	}

}
