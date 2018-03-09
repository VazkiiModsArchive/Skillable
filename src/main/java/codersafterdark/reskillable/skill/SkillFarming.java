package codersafterdark.reskillable.skill;

import codersafterdark.reskillable.skill.farming.TraitGreenThumb;
import codersafterdark.reskillable.skill.farming.TraitMoreWheat;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;

public class SkillFarming extends Skill {

    public SkillFarming() {
        super("farming", 5, new ResourceLocation("textures/blocks/dirt.png"));
    }

    @Override
    public void initUnlockables() {
        addUnlockable(new TraitMoreWheat());
        addUnlockable(new TraitGreenThumb());
    }

}
