package codersafterdark.reskillable.api.requirement.logic;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.requirement.Requirement;
import codersafterdark.reskillable.api.requirement.RequirementCache;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public abstract class DoubleRequirement extends Requirement implements OuterRequirement {
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

    protected boolean leftAchieved(EntityPlayer player) {
        return RequirementCache.requirementAchieved(player, getLeft());
    }

    protected boolean rightAchieved(EntityPlayer player) {
        return RequirementCache.requirementAchieved(player, getRight());
    }

    @Override
    public String getToolTip(PlayerData data) {
        TextFormatting color = data == null || !data.requirementAchieved(this) ? TextFormatting.RED : TextFormatting.GREEN;
        return TextFormatting.GRAY + " - " + getToolTipPart(data, getLeft()) + ' ' + color + getFormat() + ' ' + getToolTipPart(data, getRight());
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

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof DoubleRequirement) {//The order of the logic requirements does not actually matter so might as well put it here
            DoubleRequirement dreq = (DoubleRequirement) o;
            return (getRight().equals(dreq.getRight()) && getLeft().equals(dreq.getLeft())) || (getRight().equals(dreq.getLeft()) && getLeft().equals(dreq.getRight()));
        }
        return false;
    }

    @Override
    public int hashCode() {
        //Ensure there is no out of bounds errors AND that the hashcode is the same even if left and right got reversed
        long leftHash = getLeft().hashCode();
        long rightHash = getRight().hashCode();
        return (int) ((leftHash + rightHash) / 2);
    }

    @Nonnull
    @Override
    public List<Class<? extends Requirement>> getInternalTypes() {
        List<Class<? extends Requirement>> types = new ArrayList<>();
        Requirement lReq = getLeft();
        Requirement rReq = getRight();
        if (lReq instanceof OuterRequirement) {
            types.addAll(((OuterRequirement) lReq).getInternalTypes());
        } else {
            types.add(lReq.getClass());
        }
        if (rReq instanceof OuterRequirement) {
            types.addAll(((OuterRequirement) rReq).getInternalTypes());
        } else {
            types.add(rReq.getClass());
        }
        return types;
    }

    @Override
    public boolean isCacheable() {
        return getLeft().isCacheable() && getRight().isCacheable();
    }
}