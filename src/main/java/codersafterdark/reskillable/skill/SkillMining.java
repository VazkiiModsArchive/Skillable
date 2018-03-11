package codersafterdark.reskillable.skill;

import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.skill.mining.TraitFossilDigger;
import codersafterdark.reskillable.skill.mining.TraitObsidianSmasher;
import net.minecraft.util.ResourceLocation;

public class SkillMining extends Skill {

    public SkillMining() {
        super("mining", 0, new ResourceLocation("textures/blocks/stone.png"));
    }

    @Override
    public void initUnlockables() {
        addUnlockable(new TraitFossilDigger());
        addUnlockable(new TraitObsidianSmasher());

    }

}
