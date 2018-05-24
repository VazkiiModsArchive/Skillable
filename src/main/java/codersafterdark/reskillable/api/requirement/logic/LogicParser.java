package codersafterdark.reskillable.api.requirement.logic;

import codersafterdark.reskillable.api.ReskillableAPI;
import codersafterdark.reskillable.api.requirement.Requirement;
import codersafterdark.reskillable.api.requirement.RequirementComparision;
import codersafterdark.reskillable.api.requirement.RequirementRegistry;
import codersafterdark.reskillable.api.requirement.logic.impl.*;

//TODO: Make sure all TRUEs are out of the final result
//null means it is an invalid requirement/subrequirement TRUE means that it is valid but more or less will just be ignored
public class LogicParser {
    private final static FalseRequirement FALSE = new FalseRequirement();
    private final static TrueRequirement TRUE = new TrueRequirement();

    //TODO: Should we have checks of things like ((x AND y) AND (y AND z)) and simplify it to ((x AND y) AND z)
    //TODO Cont: If we decide to then we should look at other similar optimization methods

    public static Requirement parseNOT(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }
        return parseNOT(ReskillableAPI.getInstance().getRequirementRegistry().getRequirement(input));
    }

    private static Requirement parseNOT(Requirement requirement) {
        if (requirement == null) {
            return null;
        }

        if (requirement instanceof FalseRequirement) {
            return TRUE;
        } else if (requirement instanceof TrueRequirement) {
            return FALSE;
        }

        if (requirement instanceof NOTRequirement) {
            //If it is NOT of NOT just remove both NOTs
            return ((NOTRequirement) requirement).getRequirement();
        } else if (requirement instanceof DoubleRequirement) {
            DoubleRequirement orRequirement = (DoubleRequirement) requirement;
            if (requirement instanceof ORRequirement) {
                return new NORRequirement(orRequirement.getLeft(), orRequirement.getRight());
            } else if (requirement instanceof ANDRequirement) {
                return new NANDRequirement(orRequirement.getLeft(), orRequirement.getRight());
            } else if (requirement instanceof XORRequirement) {
                return new XNORRequirement(orRequirement.getLeft(), orRequirement.getRight());
            }
        }
        return new NOTRequirement(requirement);
    }

    public static Requirement parseAND(String input) {
        RequirementPair subRequirements = getSubRequirements(input);
        if (subRequirements == null) {
            return null;
        }
        Requirement left = subRequirements.getLeft();
        Requirement right = subRequirements.getRight();

        if (left instanceof FalseRequirement || right instanceof FalseRequirement) {
            return FALSE;
        } else if (left instanceof TrueRequirement) {
            return right;
        } else if (right instanceof TrueRequirement) {
            return left;
        }

        //Simplify the requirements if they are the "same" to be the one that is more restrictive
        RequirementComparision matches = left.matches(right);
        if (matches.equals(RequirementComparision.EQUAL_TO) || matches.equals(RequirementComparision.GREATER_THAN)) {
            return left;
        } else if (matches.equals(RequirementComparision.LESS_THAN)) {
            return right;
        }
        return new ANDRequirement(left, right);
    }

    public static Requirement parseNAND(String input) {
        RequirementPair subRequirements = getSubRequirements(input);
        if (subRequirements == null) {
            return null;
        }
        Requirement left = subRequirements.getLeft();
        Requirement right = subRequirements.getRight();

        if (left instanceof FalseRequirement || right instanceof FalseRequirement) {
            return TRUE;
        } else if (left instanceof TrueRequirement) {
            return parseNOT(right);
        } else if (right instanceof TrueRequirement) {
            return parseNOT(left);
        }

        if (left.matches(right).equals(RequirementComparision.EQUAL_TO)) {
            return parseNOT(left);
        }
        return new NANDRequirement(left, right);
    }

    public static Requirement parseOR(String input) {
        RequirementPair subRequirements = getSubRequirements(input);
        if (subRequirements == null) {
            return null;
        }
        Requirement left = subRequirements.getLeft();
        Requirement right = subRequirements.getRight();

        if (left instanceof TrueRequirement || right instanceof TrueRequirement) {
            return TRUE;
        } else if (left instanceof FalseRequirement) {
            return right;
        } else if (right instanceof FalseRequirement) {
            return left;
        }

        //Simplify the requirements if they are the "same" to be the one that is less restrictive
        RequirementComparision matches = left.matches(right);
        if (matches.equals(RequirementComparision.EQUAL_TO) || matches.equals(RequirementComparision.LESS_THAN)) {
            return left;
        } else if (matches.equals(RequirementComparision.GREATER_THAN)) {
            return right;
        }

        //If one is equal to the inverse of the other then it simplifies to TRUE
        if (left instanceof NOTRequirement) {
            if (!(right instanceof NOTRequirement) && ((NOTRequirement) left).getRequirement().matches(right).equals(RequirementComparision.EQUAL_TO)) {
                return TRUE;
            }
        } else if (right instanceof NOTRequirement && left.matches(((NOTRequirement) right).getRequirement()).equals(RequirementComparision.EQUAL_TO)) {
            return TRUE;
        }
        return new ORRequirement(left, right);
    }

    public static Requirement parseNOR(String input) {
        RequirementPair subRequirements = getSubRequirements(input);
        if (subRequirements == null) {
            return null;
        }
        Requirement left = subRequirements.getLeft();
        Requirement right = subRequirements.getRight();

        if (left instanceof TrueRequirement && right instanceof TrueRequirement) {
            return FALSE;
        } else if (left instanceof FalseRequirement) {
            return parseNOT(right);
        } else if (right instanceof FalseRequirement) {
            return parseNOT(left);
        }

        if (left.matches(right).equals(RequirementComparision.EQUAL_TO)) {
            return parseNOT(left);
        }
        return new NORRequirement(left, right);
    }

    public static Requirement parseXOR(String input) {
        RequirementPair subRequirements = getSubRequirements(input);
        if (subRequirements == null) {
            return null;
        }
        Requirement left = subRequirements.getLeft();
        Requirement right = subRequirements.getRight();

        if (left instanceof TrueRequirement) {
            return parseNOT(right);
        } else if (left instanceof FalseRequirement) {
            return right;
        } else if (right instanceof TrueRequirement) {
            return parseNOT(left);
        } else if (right instanceof FalseRequirement) {
            return left;
        }

        if (left.matches(right).equals(RequirementComparision.EQUAL_TO)) {
            return FALSE;
        }
        return new XORRequirement(left, right);
    }

    public static Requirement parseXNOR(String input) {
        RequirementPair subRequirements = getSubRequirements(input);
        if (subRequirements == null) {
            return null;
        }
        Requirement left = subRequirements.getLeft();
        Requirement right = subRequirements.getRight();

        if (left instanceof TrueRequirement) {
            return right;
        } else if (left instanceof FalseRequirement) {
            return parseNOT(right);
        } else if (right instanceof TrueRequirement) {
            return left;
        } else if (right instanceof FalseRequirement) {
            return parseNOT(left);
        }

        if (left.matches(right).equals(RequirementComparision.EQUAL_TO)) {
            return TRUE;
        }
        return new XNORRequirement(left, right);
    }

    //TODO implement
    private static RequirementPair getSubRequirements(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }
        RequirementRegistry registry = ReskillableAPI.getInstance().getRequirementRegistry();
        //TODO figure out format
        //[requirement]~[requirement]
        //What if NBT has either symbol will things get screwed up? (thinking of item requirements in compatskills)
        return null;
    }

    private class RequirementPair {
        private Requirement left, right;

        private RequirementPair(Requirement left, Requirement right) {
            this.left = left;
            this.right = right;
        }

        private Requirement getLeft() {
            return this.left;
        }

        private Requirement getRight() {
            return this.right;
        }
    }
}