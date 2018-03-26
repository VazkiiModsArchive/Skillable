package codersafterdark.reskillable.api.requirement;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.data.PlayerSkillInfo;
import codersafterdark.reskillable.api.skill.Skill;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
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
    public boolean achievedByPlayer(EntityPlayerMP entityPlayerMP) {
        PlayerData data = PlayerDataHandler.get(entityPlayerMP);
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

        return TextFormatting.GRAY + " - "
                + I18n.format("skillable.misc.skillFormat", color, skill.getName(), level);
    }

    public Skill getSkill() {
        return skill;
    }

    public int getLevel() {
        return level;
    }
}
