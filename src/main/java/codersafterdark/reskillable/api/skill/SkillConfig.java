package codersafterdark.reskillable.api.skill;

public class SkillConfig {
    private boolean enabled = true;
    private int levelCap = 32;
    private int skillPointInterval = 2;
    private int baseXPCost = 4;
    private int xpIncrease = 1;
    private int xpIncreaseStagger = 1;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getLevelCap() {
        return levelCap;
    }

    public void setLevelCap(int levelCap) {
        this.levelCap = levelCap;
    }

    public int getSkillPointInterval() {
        return skillPointInterval;
    }

    public void setSkillPointInterval(int skillPointInterval) {
        this.skillPointInterval = skillPointInterval;
    }

    public int getBaseXPCost() {
        return baseXPCost;
    }

    public void setBaseXPCost(int baseXPCost) {
        this.baseXPCost = baseXPCost;
    }

    public int getXpIncrease() {
        return xpIncrease;
    }

    public void setXpIncrease(int xpIncrease) {
        this.xpIncrease = xpIncrease;
    }

    public int getXpIncreaseStagger() {
        return xpIncreaseStagger;
    }

    public void setXpIncreaseStagger(int xpIncreaseStagger) {
        this.xpIncreaseStagger = xpIncreaseStagger;
    }
}
