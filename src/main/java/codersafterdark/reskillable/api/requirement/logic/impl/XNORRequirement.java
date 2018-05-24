package codersafterdark.reskillable.api.requirement.logic.impl;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.requirement.Requirement;
import codersafterdark.reskillable.api.requirement.logic.DoubleRequirement;
import net.minecraft.entity.player.EntityPlayer;

public class XNORRequirement extends DoubleRequirement {
    public XNORRequirement(Requirement left, Requirement right) {
        super(left, right);
    }

    @Override
    public boolean achievedByPlayer(EntityPlayer player) {
        return getLeft().achievedByPlayer(player) == getRight().achievedByPlayer(player);
    }

    @Override
    public String getToolTip(PlayerData data) {
        return null;
    }
}