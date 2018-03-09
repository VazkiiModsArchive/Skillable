package codersafterdark.reskillable.skill;

import codersafterdark.reskillable.skill.defense.TraitEffectTwist;
import codersafterdark.reskillable.skill.defense.TraitUndershirt;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;

public class SkillDefense extends Skill {

    public SkillDefense() {
        super("defense", 3, new ResourceLocation("textures/blocks/quartz_block_side.png"));
    }

    @Override
    public void initUnlockables() {
        addUnlockable(new TraitUndershirt());
        addUnlockable(new TraitEffectTwist());
    }

}
