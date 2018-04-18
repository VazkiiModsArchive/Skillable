package codersafterdark.reskillable.api.requirement;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.data.PlayerSkillInfo;
import codersafterdark.reskillable.api.skill.Skill;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
    @SideOnly(Side.CLIENT)
    public String getToolTip(PlayerData data) {
        PlayerSkillInfo info = data.getSkillInfo(skill);
        TextFormatting color = TextFormatting.GREEN;
        if (info.getLevel() < level) {
            color = TextFormatting.RED;
        }
        return TextFormatting.GRAY + " - " + I18n.format("skillable.misc.skillFormat", TextFormatting.DARK_AQUA, skill.getName(), color, level);
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
}
