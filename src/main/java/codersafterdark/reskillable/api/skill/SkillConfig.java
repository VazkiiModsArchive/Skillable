package codersafterdark.reskillable.api.skill;

import java.util.Map;

public class SkillConfig {
    private boolean enabled = true;
    private int levelCap = 32;
    private int skillPointInterval = 2;
    private int baseLevelCost = 4;
    private Map<Integer, Integer> levelStaggering;

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

    public int getBaseLevelCost() {
        return baseLevelCost;
    }

    public void setBaseLevelCost(int baseLevelCost) {
        this.baseLevelCost = baseLevelCost;
    }

    public Map<Integer, Integer> getLevelStaggering() {
        return levelStaggering;
    }

    public void setLevelStaggering(Map<Integer, Integer> levelStaggering) {
        this.levelStaggering = levelStaggering;
    }
}
