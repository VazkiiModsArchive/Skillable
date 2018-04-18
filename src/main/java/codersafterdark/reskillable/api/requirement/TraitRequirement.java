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
        String name = "";

        if (unlockable != null) {
            if (!data.getSkillInfo(unlockable.getParentSkill()).isUnlocked(unlockable)) {
                color = TextFormatting.RED;
            }
            name = unlockable.getName();
        }
        return TextFormatting.GRAY + " - " + TextFormatting.LIGHT_PURPLE + I18n.format("skillable.misc.traitFormat", color, name);
    }

    public Skill getSkill() {
        return unlockable.getParentSkill();
    }

    public Unlockable getUnlockable() {
        return unlockable;
    }

    @Override
    public RequirementComparision matches(Requirement other) {
        if (other instanceof TraitRequirement) {
            TraitRequirement traitRequirement = (TraitRequirement) other;
            if (getUnlockable() == null) {
                return traitRequirement.getUnlockable() == null ? RequirementComparision.EQUAL_TO : RequirementComparision.NOT_EQUAL;
            }
            return traitRequirement.getUnlockable() != null && getUnlockable().getKey().equals(traitRequirement.getUnlockable().getKey()) ? RequirementComparision.EQUAL_TO : RequirementComparision.NOT_EQUAL;
        }
        return RequirementComparision.NOT_EQUAL;
    }
}
