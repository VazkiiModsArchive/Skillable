package codersafterdark.reskillable.skill;

import codersafterdark.reskillable.skill.farming.TraitGreenThumb;
import codersafterdark.reskillable.skill.farming.TraitMoreWheat;
import net.minecraft.init.Blocks;

public class SkillFarming extends Skill {

    public SkillFarming() {
        super("farming", 5, Blocks.DIRT);
    }

    @Override
    public void initUnlockables() {
        addUnlockable(new TraitMoreWheat());
        addUnlockable(new TraitGreenThumb());
    }

}
