package codersafterdark.reskillable.api.requirement;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.skill.Skill;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

import java.util.Objects;

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
            if (skill == null || skillRequirement.skill == null) {
                //If they are both invalid don't bother checking the level.
                return RequirementComparision.NOT_EQUAL;
            }
            if (skill.getKey().equals(skillRequirement.skill.getKey())) {
                if (level == skillRequirement.level) {
                    return RequirementComparision.EQUAL_TO;
                }
                return level > skillRequirement.level ? RequirementComparision.GREATER_THAN : RequirementComparision.LESS_THAN;
            }
        }
        return RequirementComparision.NOT_EQUAL;
    }

    @Override
    public boolean isEnabled() {
        return skill != null && skill.isEnabled();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof SkillRequirement) {
            SkillRequirement sReq = (SkillRequirement) o;
            return skill.equals(sReq.skill) && level == sReq.level;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(skill, level);
    }
}