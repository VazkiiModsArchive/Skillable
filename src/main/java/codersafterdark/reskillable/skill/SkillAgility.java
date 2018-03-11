package codersafterdark.reskillable.skill;

import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.skill.agility.TraitRoadWalk;
import codersafterdark.reskillable.skill.agility.TraitSidestep;
import net.minecraft.util.ResourceLocation;

public class SkillAgility extends Skill {

    public SkillAgility() {
        super("agility", 6, new ResourceLocation("textures/blocks/gravel.png"));
    }

    @Override
    public void initUnlockables() {
        addUnlockable(new TraitRoadWalk());
        addUnlockable(new TraitSidestep());
    }
}