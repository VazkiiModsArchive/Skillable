package codersafterdark.reskillable.skill;

import codersafterdark.reskillable.api.skill.Skill;
import net.minecraft.util.ResourceLocation;

import static codersafterdark.reskillable.lib.LibMisc.MOD_ID;

public class SkillBuilding extends Skill {
    public SkillBuilding() {
        super(new ResourceLocation(MOD_ID, "building"), new ResourceLocation("textures/blocks/brick.png"));
    }
}