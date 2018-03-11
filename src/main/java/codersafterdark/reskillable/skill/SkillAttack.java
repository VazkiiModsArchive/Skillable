package codersafterdark.reskillable.skill;

import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.skill.attack.TraitBattleSpirit;
import codersafterdark.reskillable.skill.attack.TraitNeutralissse;
import net.minecraft.util.ResourceLocation;

public class SkillAttack extends Skill {

    public SkillAttack() {
        super("attack", 2, new ResourceLocation("textures/blocks/stonebrick.png"));
    }

    @Override
    public void initUnlockables() {
        addUnlockable(new TraitNeutralissse());
        addUnlockable(new TraitBattleSpirit());
    }

}
