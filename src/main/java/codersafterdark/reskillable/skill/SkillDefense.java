package codersafterdark.reskillable.skill;

import codersafterdark.reskillable.skill.defense.TraitEffectTwist;
import codersafterdark.reskillable.skill.defense.TraitUndershirt;
import net.minecraft.init.Blocks;

public class SkillDefense extends Skill {

    public SkillDefense() {
        super("defense", 3, Blocks.QUARTZ_BLOCK);
    }

    @Override
    public void initUnlockables() {
        addUnlockable(new TraitUndershirt());
        addUnlockable(new TraitEffectTwist());
    }

}
