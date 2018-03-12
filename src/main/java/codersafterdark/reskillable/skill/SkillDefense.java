package codersafterdark.reskillable.skill;

import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.skill.defense.TraitEffectTwist;
import codersafterdark.reskillable.skill.defense.TraitUndershirt;
import net.minecraft.util.ResourceLocation;

public class SkillDefense extends Skill {

    public SkillDefense() {
        super("defense", 3, new ResourceLocation("textures/blocks/quartz_block_side.png"));
    }
}
