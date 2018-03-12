package codersafterdark.reskillable.skill;

import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.skill.mining.TraitFossilDigger;
import codersafterdark.reskillable.skill.mining.TraitObsidianSmasher;
import net.minecraft.util.ResourceLocation;

import static codersafterdark.reskillable.lib.LibMisc.MOD_ID;

public class SkillMining extends Skill {

    public SkillMining() {
        super(new ResourceLocation(MOD_ID,"mining"), new ResourceLocation("textures/blocks/stone.png"));
    }
}
