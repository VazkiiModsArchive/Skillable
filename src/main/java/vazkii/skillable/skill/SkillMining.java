package vazkii.skillable.skill;

import net.minecraft.init.Blocks;
import vazkii.skillable.skill.mining.TraitFossilDigger;
import vazkii.skillable.skill.mining.TraitObsidianSmasher;

public class SkillMining extends Skill {

    public SkillMining() {
        super("mining", 0, Blocks.STONE);
    }

    @Override
    public void initUnlockables() {
        addUnlockable(new TraitFossilDigger());
        addUnlockable(new TraitObsidianSmasher());

//		addUnlockable(new AbilityOreCascade());
    }

}
