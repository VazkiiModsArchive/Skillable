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

    protected abstract String getFormat();

    @Override
    public String getToolTip(PlayerData data) {
        return TextFormatting.GRAY + " - " + getToolTipPart(data, getLeft()) + ' ' + TextFormatting.GOLD + getFormat() + ' ' + getToolTipPart(data, getRight());
    }

    private String getToolTipPart(PlayerData data, Requirement side) {
        String tooltip = side.getToolTip(data);
        if (tooltip != null && tooltip.startsWith(TextFormatting.GRAY + " - ")) {
            tooltip = tooltip.replaceFirst(TextFormatting.GRAY + " - ", "");
        }
        if (side instanceof DoubleRequirement) {
            tooltip = TextFormatting.GOLD + "(" + TextFormatting.RESET + tooltip + TextFormatting.GOLD + ')';
        } else {
            //Ensure that no color leaks
            tooltip = TextFormatting.RESET + tooltip;
        }
        return tooltip;
    }
}