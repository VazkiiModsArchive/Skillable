package codersafterdark.reskillable.advancement.skilllevel;

import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.lib.LibMisc;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class SkillLevelCriterionInstance extends AbstractCriterionInstance {
    private final Skill skill;
    private final int level;

    public SkillLevelCriterionInstance(@Nullable Skill skill, int level) {
        super(new ResourceLocation(LibMisc.MOD_ID, "skill_level"));

        this.skill = skill;
        this.level = level;
    }

    public boolean test(final Skill skill, final int level) {
        return (this.skill == null || this.skill == skill) && level >= this.level;
    }
}
