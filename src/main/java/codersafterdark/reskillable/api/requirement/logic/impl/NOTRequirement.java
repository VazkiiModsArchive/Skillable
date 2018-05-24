package codersafterdark.reskillable.api.requirement.logic.impl;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.requirement.Requirement;
import net.minecraft.entity.player.EntityPlayer;

public class NOTRequirement extends Requirement {
    private final Requirement requirement;

    public NOTRequirement(Requirement requirement) {
        this.requirement = requirement;
    }

    @Override
    public boolean achievedByPlayer(EntityPlayer player) {
        return !this.requirement.achievedByPlayer(player);
    }

    @Override
    public String getToolTip(PlayerData data) {
        String parentToolTip = this.requirement.getToolTip(data);
        //TODO: Make it color it properly, maybe have some way of inverting the two colors
        return '!' + parentToolTip;
    }

    public Requirement getRequirement() {
        return this.requirement;
    }
}