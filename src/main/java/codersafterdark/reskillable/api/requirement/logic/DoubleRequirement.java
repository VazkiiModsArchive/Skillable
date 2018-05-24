package codersafterdark.reskillable.api.requirement.logic;

import codersafterdark.reskillable.api.requirement.Requirement;

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
}