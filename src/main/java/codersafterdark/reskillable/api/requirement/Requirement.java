package codersafterdark.reskillable.api.requirement;

import codersafterdark.reskillable.api.data.PlayerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;

public abstract class Requirement {
    protected String tooltip = "";

    public abstract boolean achievedByPlayer(EntityPlayer entityPlayerMP);

    public String getToolTip(PlayerData data) {
        try {
            return String.format(internalToolTip(), data == null || !data.requirementAchieved(this) ? TextFormatting.RED : TextFormatting.GREEN);
        } catch (IllegalArgumentException e) {
            return internalToolTip(); //If the formatting code is not there just return whatever the internal String is
        }
    }

    public RequirementComparision matches(Requirement other) {
        return equals(other) ? RequirementComparision.EQUAL_TO : RequirementComparision.NOT_EQUAL;
    }

    //Mainly used for skills and traits. If this is false then using this requirement will log an error and ignore the requirement
    public boolean isEnabled() {
        return true;
    }

    //Should only be used if people know what they are doing
    public final String internalToolTip() {
        return tooltip;
    }

    public boolean isCacheable() {
        return true;
    }
}