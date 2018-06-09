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
        try {
            //Just use the opposite coloring for the parent tooltip. Allows for red and green coloring of requirement name
            return String.format(this.requirement.internalToolTip(), data == null || !data.requirementAchieved(this) ? TextFormatting.RED : TextFormatting.GREEN);
        } catch (IllegalArgumentException e) {
            //If it fails fall back to old method of inverting the colors
            String parentToolTip = this.requirement.getToolTip(data);
            if (parentToolTip == null) {
                return "";
            }
            if (parentToolTip.startsWith(TextFormatting.GRAY + " - ")) {
                parentToolTip = parentToolTip.replaceFirst(TextFormatting.GRAY + " - ", "");
            }
            char colorCode = '\u00a7';
            String start = TextFormatting.GRAY + " - ";
            if (parentToolTip.length() > 2 && parentToolTip.startsWith(Character.toString(colorCode))) {
                start += parentToolTip.substring(0, 2);
                parentToolTip = parentToolTip.substring(2);
            }
            start += '!';
            StringBuilder tooltip = new StringBuilder(start);
            char[] chars = parentToolTip.toCharArray();
            char lastChar = '!';
            char red = 'c';
            char green = 'a';
            for (char c : chars) {
                if (lastChar == colorCode && (c == red || c == green)) {
                    if (c == red) {
                        tooltip.append(green);
                    } else {
                        tooltip.append(red);
                    }
                } else {
                    tooltip.append(c);
                }
                lastChar = c;
            }
            return tooltip.toString();
        }
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