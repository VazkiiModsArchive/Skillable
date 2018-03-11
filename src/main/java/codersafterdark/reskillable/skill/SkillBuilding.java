package codersafterdark.reskillable.skill;

import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.skill.building.TraitChorusTransmutation;
import codersafterdark.reskillable.skill.building.TraitPerfectRecover;
import net.minecraft.util.ResourceLocation;

public class SkillBuilding extends Skill {

    public SkillBuilding() {
        super("building", 4, new ResourceLocation("textures/blocks/brick.png"));
    }

    @Override
    public void initUnlockables() {
        addUnlockable(new TraitChorusTransmutation());
        addUnlockable(new TraitPerfectRecover());
    }

}
