package codersafterdark.reskillable.api.requirement;

import codersafterdark.reskillable.api.data.PlayerData;
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
    }

    @Override
    public boolean achievedByPlayer(EntityPlayer entityPlayer) {
        PlayerData data = PlayerDataHandler.get(entityPlayer);
        return data.getSkillInfo(skill).getLevel() >= level;
    }

    @Override
    public String getToolTip(PlayerData data) {
        TextFormatting color = TextFormatting.GREEN;
        if (data == null || data.getSkillInfo(skill).getLevel() < level) {
            color = TextFormatting.RED;
        }
        return TextFormatting.GRAY + " - " + new TextComponentTranslation("skillable.misc.skillFormat", TextFormatting.DARK_AQUA, skill.getName(), color, level).getUnformattedComponentText();
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
