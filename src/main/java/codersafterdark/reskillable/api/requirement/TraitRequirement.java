package codersafterdark.reskillable.api.requirement;

import codersafterdark.reskillable.api.ReskillableRegistries;
import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.api.unlockable.Unlockable;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TraitRequirement extends Requirement {
    private Unlockable unlockable;

    public TraitRequirement(ResourceLocation traitName) {
        this.unlockable = ReskillableRegistries.UNLOCKABLES.getValue(traitName);
    }

    @Override
    public boolean achievedByPlayer(EntityPlayer entityPlayer) {
        PlayerData data = PlayerDataHandler.get(entityPlayer);
        return data.getSkillInfo(unlockable.getParentSkill()).isUnlocked(unlockable);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getToolTip(PlayerData data) {
        Unlockable unlockable = getUnlockable();
        TextFormatting color = TextFormatting.GREEN;
        String toolTip = "";

        if (unlockable != null) {
            if (!data.getSkillInfo(unlockable.getParentSkill()).isUnlocked(unlockable)) {
                color = TextFormatting.RED;
            }
        }
        return TextFormatting.GRAY + " - " + TextFormatting.LIGHT_PURPLE + I18n.format("skillable.misc.traitFormat", color, unlockable.getName());
    }

    public Skill getSkill() {
        return unlockable.getParentSkill();
    }

    public Unlockable getUnlockable() {
        return unlockable;
    }

    @Override
    public RequirementCompare matches(Requirement other) {
        return other instanceof TraitRequirement && getUnlockable().getKey().equals(((TraitRequirement) other).getUnlockable().getKey()) ?
                RequirementCompare.EQUAL_TO : RequirementCompare.NOT_EQUAL;
    }
}
