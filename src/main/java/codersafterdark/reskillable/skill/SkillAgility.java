package codersafterdark.reskillable.skill;

import codersafterdark.reskillable.skill.agility.TraitRoadwalk;
import codersafterdark.reskillable.skill.agility.TraitSidestep;
import net.minecraft.init.Blocks;

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