package codersafterdark.reskillable.api.requirement;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.skill.Skill;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class SkillRequirement extends Requirement {
    private final Skill skill;
    private final int level;

    public SkillRequirement(Skill skill, int level) {
        this.skill = skill;
        this.level = level;
        this.tooltip = TextFormatting.GRAY + " - " + new TextComponentTranslation("skillable.misc.skillFormat", TextFormatting.DARK_AQUA, skill.getName(),
                "%s", level).getUnformattedComponentText();
    }

    @Override
    public boolean achievedByPlayer(EntityPlayer entityPlayer) {
        return PlayerDataHandler.get(entityPlayer).getSkillInfo(skill).getLevel() >= level;
    }

    public Skill getSkill() {
        return skill;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public RequirementComparision matches(Requirement other) {
        if (other instanceof SkillRequirement) {
            SkillRequirement skillRequirement = (SkillRequirement) other;
            if (getSkill() == null || skillRequirement.getSkill() == null) {
                //If they are both invalid don't bother checking the level.
                return RequirementComparision.NOT_EQUAL;
            }
            if (getSkill().getKey().equals(skillRequirement.getSkill().getKey())) {
                if (getLevel() == skillRequirement.getLevel()) {
                    return RequirementComparision.EQUAL_TO;
                }
                return getLevel() > skillRequirement.getLevel() ? RequirementComparision.GREATER_THAN : RequirementComparision.LESS_THAN;
            }
        }
        return RequirementComparision.NOT_EQUAL;
    }

    @Override
    public boolean isEnabled() {
        return getSkill() != null && getSkill().isEnabled();
    }
}