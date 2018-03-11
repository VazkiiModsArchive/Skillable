package codersafterdark.reskillable.skill;

import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.skill.gathering.TraitDropGuarantee;
import codersafterdark.reskillable.skill.gathering.TraitLuckyFisherman;
import net.minecraft.util.ResourceLocation;

public class SkillGathering extends Skill {

    public SkillGathering() {
        super("gathering", 1, new ResourceLocation("textures/blocks/log_oak.png"));
    }

    @Override
    public void initUnlockables() {
        addUnlockable(new TraitLuckyFisherman());
        addUnlockable(new TraitDropGuarantee());
    }

}
