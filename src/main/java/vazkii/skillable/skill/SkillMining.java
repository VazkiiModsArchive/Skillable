package vazkii.skillable.skill;

import net.minecraft.init.Blocks;
import vazkii.skillable.skill.mining.AbilityOreCascade;
import vazkii.skillable.skill.mining.TraitFossilDigger;
import vazkii.skillable.skill.mining.TraitObsidianSmasher;

public class SkillMining extends Skill {

	public SkillMining() {
		super("mining", 0, Blocks.STONE);
	}

	@Override
	public void initUnlockables() {
		unlockables.add(new TraitFossilDigger());
		unlockables.add(new TraitObsidianSmasher());
		
		unlockables.add(new AbilityOreCascade());
	}
	
}
