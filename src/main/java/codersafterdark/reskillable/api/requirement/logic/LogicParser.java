package codersafterdark.reskillable.api.requirement.logic;

import codersafterdark.reskillable.api.ReskillableAPI;
import codersafterdark.reskillable.api.requirement.Requirement;
import codersafterdark.reskillable.api.requirement.RequirementComparision;
import codersafterdark.reskillable.api.requirement.RequirementException;
import codersafterdark.reskillable.api.requirement.RequirementRegistry;
import codersafterdark.reskillable.api.requirement.logic.impl.*;

//TODO: Make it so that if a requirement is simplified it logs what the new string is so that pack makers can know the simpler string
//null means it is an invalid requirement/subrequirement TRUE means that it is valid but more or less will just be ignored
public class LogicParser {
    public final static FalseRequirement FALSE = new FalseRequirement();
    public final static TrueRequirement TRUE = new TrueRequirement();

    //TODO: Should we have checks of things like ((x AND y) AND (y AND z)) and simplify it to ((x AND y) AND z)
    //TODO Cont: If we decide to then we should look at other similar optimization methods

    public static Requirement parseNOT(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }
        return parseNOT(ReskillableAPI.getInstance().getRequirementRegistry().getRequirement(input));
    }

    //TODO: Decide if it should replace parts of the string to auto simplify parts before getting the requirements. Would make there be slightly less calls to getRequirement
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
            DoubleRequirement doubleRequirement = (DoubleRequirement) requirement;
            //Switch the requirement to be an inversion for better performance when checking if achieved
            if (requirement instanceof ANDRequirement) {
                return new NANDRequirement(doubleRequirement.getLeft(), doubleRequirement.getRight());
            } else if (requirement instanceof NANDRequirement) {
                return new ANDRequirement(doubleRequirement.getLeft(), doubleRequirement.getRight());
            } else if (requirement instanceof ORRequirement) {
                return new NORRequirement(doubleRequirement.getLeft(), doubleRequirement.getRight());
            } else if (requirement instanceof NORRequirement) {
                return new ORRequirement(doubleRequirement.getLeft(), doubleRequirement.getRight());
            } else if (requirement instanceof XORRequirement) {
                return new XNORRequirement(doubleRequirement.getLeft(), doubleRequirement.getRight());
            } else if (requirement instanceof XNORRequirement) {
                return new XORRequirement(doubleRequirement.getLeft(), doubleRequirement.getRight());
            }
        }
        return new NOTRequirement(requirement);
    }

    public static Requirement parseAND(String input) throws RequirementException {
        RequirementPair subRequirements = getSubRequirements(input);
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

    public static Requirement parseNAND(String input) throws RequirementException {
        RequirementPair subRequirements = getSubRequirements(input);
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

    public static Requirement parseOR(String input) throws RequirementException {
        RequirementPair subRequirements = getSubRequirements(input);
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

    public static Requirement parseNOR(String input) throws RequirementException {
        RequirementPair subRequirements = getSubRequirements(input);
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

    public static Requirement parseXOR(String input) throws RequirementException {
        RequirementPair subRequirements = getSubRequirements(input);
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

    public static Requirement parseXNOR(String input) throws RequirementException {
        RequirementPair subRequirements = getSubRequirements(input);
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

    private static RequirementPair getSubRequirements(String input) throws RequirementException {
        //[requirement]~[requirement]
        //[ -> Count if at start or after '|' or '~'
        //] -> Count if at end or before '~'
        if (input == null || input.length() < 5 || !input.startsWith("[") || !input.endsWith("]")) {
            throw new RequirementException("Invalid format for double requirement.");
        }
        String first = "";
        int count = 1;//The first bracket
        char lastChar = '[';
        int secondStart = 4;
        int closeBrackets = 0;
        for (int i = 1; i < input.length(); i++) {
            char c = input.charAt(i);
            if (lastChar == ']') {
                if (c == ']') {
                    closeBrackets++;
                } else {
                    if (c == '~') {
                        count = count - 1 - closeBrackets;
                    }
                    closeBrackets = 0;
                }
            } else {
                if ((lastChar == '|' || lastChar == '~') && c == '[') {
                    count++;
                }
                closeBrackets = 0;
            }
            if (count == 0) {
                if (!first.isEmpty()) {
                    throw new RequirementException("Something went wrong, double check brackets.");
                }
                first = input.substring(1, i - 1);
                secondStart = i + 2;
            }
            lastChar = c;
        }
        count = count - 1 - closeBrackets;
        if (count != 0) {
            throw new RequirementException("Mismatched brackets.");
        }

        RequirementRegistry registry = ReskillableAPI.getInstance().getRequirementRegistry();
        Requirement left = registry.getRequirement(first);
        if (left == null) {
            throw new RequirementException("Invalid left-hand requirement '" + first + "'.");
        }
        String second = input.substring(secondStart, input.length() - 1);
        Requirement right = registry.getRequirement(second);
        if (right == null) {
            throw new RequirementException("Invalid right-hand requirement '" + second + "'.");
        }
        return new RequirementPair(left, right);
    }

    private static class RequirementPair {
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