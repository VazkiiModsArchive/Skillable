package codersafterdark.reskillable.api.unlockable;

import codersafterdark.reskillable.api.data.RequirementHolder;

public class UnlockableConfig {
    private boolean enabled = true;
    private int x = 1;
    private int y = 1;
    private int cost = 1;
    private RequirementHolder requirementHolder = RequirementHolder.realEmpty();

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public RequirementHolder getRequirementHolder() {
        return requirementHolder;
    }

    public void setRequirementHolder(RequirementHolder requirementHolder) {
        this.requirementHolder = requirementHolder;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
