package codersafterdark.reskillable.skill;

import codersafterdark.reskillable.skill.mining.TraitFossilDigger;
import codersafterdark.reskillable.skill.mining.TraitObsidianSmasher;
import net.minecraft.init.Blocks;

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
