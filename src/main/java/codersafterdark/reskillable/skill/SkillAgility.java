package codersafterdark.reskillable.skill;

import codersafterdark.reskillable.api.skill.Skill;
import net.minecraft.util.ResourceLocation;

import static codersafterdark.reskillable.lib.LibMisc.MOD_ID;

public class SkillAgility extends Skill {

    public SkillAgility() {
        super(new ResourceLocation(MOD_ID,"agility"),  new ResourceLocation("textures/blocks/gravel.png"));
    }
}