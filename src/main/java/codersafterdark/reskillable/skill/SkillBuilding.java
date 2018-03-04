package codersafterdark.reskillable.skill;

import codersafterdark.reskillable.skill.building.TraitChorusTransmutation;
import codersafterdark.reskillable.skill.building.TraitPerfectRecover;
import net.minecraft.init.Blocks;

public class SkillBuilding extends Skill {

    public SkillBuilding() {
        super("building", 4, Blocks.BRICK_BLOCK);
    }

    @Override
    public void initUnlockables() {
        addUnlockable(new TraitChorusTransmutation());
        addUnlockable(new TraitPerfectRecover());
    }

}
