package codersafterdark.reskillable.skill;

import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.skill.gathering.TraitDropGuarantee;
import codersafterdark.reskillable.skill.gathering.TraitLuckyFisherman;
import net.minecraft.util.ResourceLocation;

import static codersafterdark.reskillable.lib.LibMisc.MOD_ID;

public class SkillGathering extends Skill {

    public SkillGathering() {
        super(new ResourceLocation(MOD_ID,"gathering"), new ResourceLocation("textures/blocks/log_oak.png"));
    }
}
