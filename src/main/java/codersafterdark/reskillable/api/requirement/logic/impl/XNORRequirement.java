package codersafterdark.reskillable.api.requirement.logic.impl;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.requirement.Requirement;
import codersafterdark.reskillable.api.requirement.RequirementComparision;
import codersafterdark.reskillable.api.requirement.logic.DoubleRequirement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;

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
        return getLeft().getToolTip(data) + new TextComponentTranslation("reskillable.misc.xnorFormat").getUnformattedComponentText() + getRightToolTip(data);
    }

    @Override
    public RequirementComparision matches(Requirement o) {
        if (o instanceof ORRequirement) {
            ORRequirement other = (ORRequirement) o;
            RequirementComparision left = getLeft().matches(other.getLeft());
            RequirementComparision right = getRight().matches(other.getRight());
            boolean same = left.equals(right);
            if (same && left.equals(RequirementComparision.EQUAL_TO)) {
                return RequirementComparision.EQUAL_TO;
            }

            //Check to see if they were just written in the opposite order
            RequirementComparision leftAlt = getLeft().matches(other.getRight());
            RequirementComparision rightAlt = getRight().matches(other.getLeft());
            boolean altSame = leftAlt.equals(rightAlt);
            if (altSame && leftAlt.equals(RequirementComparision.EQUAL_TO)) {
                return RequirementComparision.EQUAL_TO;
            }

            //XNOR specific check

        }
        return RequirementComparision.NOT_EQUAL;
    }
}