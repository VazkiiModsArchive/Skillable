package codersafterdark.reskillable.api.requirement.logic;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.requirement.Requirement;
import codersafterdark.reskillable.api.requirement.RequirementComparision;
import net.minecraft.entity.player.EntityPlayer;

public class TrueRequirement extends Requirement {
    @Override
    public boolean achievedByPlayer(EntityPlayer entityPlayerMP) {
        return true;
    }

    @Override
    public String getToolTip(PlayerData data) {
        return null;
    }

    @Override
    public RequirementComparision matches(Requirement other) {
        return other instanceof TrueRequirement ? RequirementComparision.EQUAL_TO : RequirementComparision.NOT_EQUAL;
    }
}