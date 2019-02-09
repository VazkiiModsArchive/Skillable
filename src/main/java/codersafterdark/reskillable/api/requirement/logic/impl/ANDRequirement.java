package codersafterdark.reskillable.api.requirement.logic.impl;

import codersafterdark.reskillable.api.requirement.Requirement;
import codersafterdark.reskillable.api.requirement.RequirementComparision;
import codersafterdark.reskillable.api.requirement.logic.DoubleRequirement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;

public class ANDRequirement extends DoubleRequirement {
    public ANDRequirement(Requirement left, Requirement right) {
        super(left, right);
    }

    @Override
    public boolean achievedByPlayer(EntityPlayer player) {
        return leftAchieved(player) && rightAchieved(player);
    }

    @Override
    protected String getFormat() {
        return new TextComponentTranslation("reskillable.requirements.format.and").getUnformattedComponentText();
    }

    //TODO: Figure out how to implement this in the other logic requirements for if the elements are not just the same
    @Override
    public RequirementComparision matches(Requirement o) {
        if (o instanceof ANDRequirement) {
            ANDRequirement other = (ANDRequirement) o;
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

            //AND specific check
            //Check to see if one is greater/less than for both sub requirements
            if ((same && left.equals(RequirementComparision.GREATER_THAN)) || (altSame && leftAlt.equals(RequirementComparision.GREATER_THAN))) {
                return RequirementComparision.GREATER_THAN;
            } else if ((same && left.equals(RequirementComparision.LESS_THAN)) || (altSame && leftAlt.equals(RequirementComparision.LESS_THAN))) {
                return RequirementComparision.LESS_THAN;
            }
        }
        return RequirementComparision.NOT_EQUAL;
    }
}