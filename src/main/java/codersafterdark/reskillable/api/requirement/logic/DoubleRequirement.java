package codersafterdark.reskillable.api.requirement.logic;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.requirement.Requirement;
import net.minecraft.util.text.TextFormatting;

public abstract class DoubleRequirement extends Requirement {
    private final Requirement left, right;

    protected DoubleRequirement(Requirement left, Requirement right) {
        this.left = left;
        this.right = right;
    }

    public Requirement getLeft() {
        return this.left;
    }

    public Requirement getRight() {
        return this.right;
    }

    protected String getRightToolTip(PlayerData data) {
        String tooltip = getRight().getToolTip(data);
        if (tooltip != null && tooltip.startsWith(TextFormatting.GRAY + " - ")) {
            tooltip = tooltip.replaceFirst(TextFormatting.GRAY + " - ", "");
        }
        return TextFormatting.RESET + " " + tooltip;
    }

    protected String getLeftToolTip(PlayerData data) {
        String tooltip = getLeft().getToolTip(data);
        if (tooltip != null && !tooltip.startsWith(TextFormatting.GRAY + " - ")) {
            tooltip = TextFormatting.GRAY + " - " + tooltip;
        }
        return tooltip + ' ' + TextFormatting.GOLD;
    }
}