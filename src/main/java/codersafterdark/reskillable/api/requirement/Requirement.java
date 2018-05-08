package codersafterdark.reskillable.api.requirement;

import codersafterdark.reskillable.api.data.PlayerData;
import net.minecraft.entity.player.EntityPlayer;

public abstract class Requirement {
    public abstract boolean achievedByPlayer(EntityPlayer entityPlayerMP);

    public abstract String getToolTip(PlayerData data);

    public RequirementComparision matches(Requirement other) {
        return equals(other) ? RequirementComparision.EQUAL_TO : RequirementComparision.NOT_EQUAL;
    }

    //Mainly used for skills and traits. If this is false then using this requirement will log an error and ignore the requirement
    public boolean isEnabled() {
        return true;
    }
}
