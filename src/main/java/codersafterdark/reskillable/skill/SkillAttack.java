package codersafterdark.reskillable.skill;

import codersafterdark.reskillable.skill.attack.TraitBattleSpirit;
import codersafterdark.reskillable.skill.attack.TraitNeutralissse;
import net.minecraft.init.Blocks;

public class SkillAttack extends Skill {

    public SkillAttack() {
        super("attack", 2, Blocks.STONEBRICK);
    }

    @Override
    public void initUnlockables() {
        addUnlockable(new TraitNeutralissse());
        addUnlockable(new TraitBattleSpirit());
    }

}
