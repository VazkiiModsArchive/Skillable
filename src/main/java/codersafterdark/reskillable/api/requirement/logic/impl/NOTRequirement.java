package codersafterdark.reskillable.api.requirement.logic.impl;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.requirement.Requirement;
import codersafterdark.reskillable.api.requirement.RequirementComparision;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;

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
        if (parentToolTip == null) {
            return "";
        }
        if (parentToolTip.startsWith(TextFormatting.GRAY + " - ")) {
            parentToolTip = parentToolTip.replaceFirst(TextFormatting.GRAY + " - ", "");
        }
        if (parentToolTip.length() > 2 && parentToolTip.startsWith("\u00a7")) {
            parentToolTip = parentToolTip.substring(0, 2) + '!' + parentToolTip.substring(2);
        } else {
            parentToolTip = '!' + parentToolTip;
        }
        parentToolTip = TextFormatting.GRAY + " - " + parentToolTip;

        String green = TextFormatting.GREEN.toString();
        String red = TextFormatting.RED.toString();
        int index = 0;
        while (index < parentToolTip.length()) {
            int greenIndex = parentToolTip.indexOf(green, index);
            int redIndex = parentToolTip.indexOf(red, index);
            if (greenIndex >= 0 && (greenIndex < redIndex || redIndex < 0)) {
                String end = greenIndex + 2 > parentToolTip.length() ? "" : parentToolTip.substring(greenIndex + 2);
                parentToolTip = parentToolTip.substring(index, greenIndex) + red + end;
                index = greenIndex + 2;
            } else {
                if (redIndex < 0) {
                    break;
                }
                String end = redIndex + 2 > parentToolTip.length() ? "" : parentToolTip.substring(redIndex + 2);
                parentToolTip = parentToolTip.substring(index, redIndex) + green + end;
                index = redIndex + 2;
            }
        }
        return parentToolTip;
    }

    public Requirement getRequirement() {
        return this.requirement;
    }

    @Override
    public RequirementComparision matches(Requirement o) {
        if (o instanceof NOTRequirement) {
            RequirementComparision match = getRequirement().matches(((NOTRequirement) o).getRequirement());
            switch (match) {
                case GREATER_THAN:
                    return RequirementComparision.LESS_THAN;
                case LESS_THAN:
                    return RequirementComparision.GREATER_THAN;
                default:
                    return match;
            }
        }
        return RequirementComparision.NOT_EQUAL;
    }
}