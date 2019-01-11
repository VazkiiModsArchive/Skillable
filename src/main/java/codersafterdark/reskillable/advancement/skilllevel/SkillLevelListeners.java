package codersafterdark.reskillable.advancement.skilllevel;

import codersafterdark.reskillable.advancement.CriterionListeners;
import codersafterdark.reskillable.api.skill.Skill;
import net.minecraft.advancements.PlayerAdvancements;

public class SkillLevelListeners extends CriterionListeners<SkillLevelCriterionInstance> {
    public SkillLevelListeners(PlayerAdvancements playerAdvancements) {
        super(playerAdvancements);
    }

    public void trigger(final Skill skill, final int level) {
        trigger(instance -> instance.test(skill, level));
    }
}
