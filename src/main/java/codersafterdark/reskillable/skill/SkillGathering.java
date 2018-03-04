package codersafterdark.reskillable.skill;

import codersafterdark.reskillable.skill.gathering.TraitDropGuarantee;
import codersafterdark.reskillable.skill.gathering.TraitLuckyFisherman;
import net.minecraft.init.Blocks;

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
