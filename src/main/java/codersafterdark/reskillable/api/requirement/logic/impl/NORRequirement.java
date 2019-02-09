package codersafterdark.reskillable.api.requirement.logic.impl;

import codersafterdark.reskillable.api.requirement.Requirement;
import codersafterdark.reskillable.api.requirement.RequirementComparision;
import codersafterdark.reskillable.api.requirement.logic.DoubleRequirement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;

public class NORRequirement extends DoubleRequirement {
    public NORRequirement(Requirement left, Requirement right) {
        super(left, right);
    }

    @Override
    public boolean achievedByPlayer(EntityPlayer player) {
        return !leftAchieved(player) && !rightAchieved(player);
    }

    @Override
    protected String getFormat() {
        return new TextComponentTranslation("reskillable.requirements.format.nor").getUnformattedComponentText();
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

            //NOR specific check

        }
        return RequirementComparision.NOT_EQUAL;
    }
}