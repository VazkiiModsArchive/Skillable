package vazkii.skillable.base;

import net.minecraft.nbt.NBTTagCompound;
import vazkii.skillable.skill.Skill;
import vazkii.skillable.skill.Skills;
import vazkii.skillable.skill.base.Ability;
import vazkii.skillable.skill.base.IAbilityEventHandler;
import vazkii.skillable.skill.base.Unlockable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class PlayerSkillInfo {

    private static final String TAG_LEVEL = "level";
    private static final String TAG_SKILL_POINTS = "skillPoints";
    private static final String TAG_UNLOCKABLES = "unlockables";

    public final Skill skill;

    private int level;
    private int skillPoints;
    private List<Unlockable> unlockables = new ArrayList();

    public PlayerSkillInfo(Skill skill) {
        this.skill = skill;
        level = 1;
        respec();
    }

    public void loadFromNBT(NBTTagCompound cmp) {
        level = cmp.getInteger(TAG_LEVEL);
        skillPoints = cmp.getInteger(TAG_SKILL_POINTS);

        unlockables.clear();
        NBTTagCompound unlockablesCmp = cmp.getCompoundTag(TAG_UNLOCKABLES);
        for (String s : unlockablesCmp.getKeySet())
            if (Skills.ALL_UNLOCKABLES.containsKey(s))
                unlockables.add(Skills.ALL_UNLOCKABLES.get(s));
    }

    public void saveToNBT(NBTTagCompound cmp) {
        cmp.setInteger(TAG_LEVEL, level);
        cmp.setInteger(TAG_SKILL_POINTS, skillPoints);

        NBTTagCompound unlockablesCmp = new NBTTagCompound();
        for (Unlockable u : unlockables)
            unlockablesCmp.setBoolean(u.getKey(), true);
        cmp.setTag(TAG_UNLOCKABLES, unlockablesCmp);
    }

    public int getLevel() {
        if (level <= 0)
            level = 1;
        if (level > ConfigHandler.levelCap)
            level = ConfigHandler.levelCap;

        return level;
    }

    public int getRank() {
        return level / (ConfigHandler.levelCap / 7);
    }

    public int getSkillPoints() {
        return skillPoints;
    }

    public boolean isCapped() {
        return level == ConfigHandler.levelCap;
    }

    public int getLevelUpCost() {
        return ConfigHandler.baseXPCost + Math.max(0, (level - 1) / ConfigHandler.xpIncreaseStagger) * ConfigHandler.xpIncrease;
    }

    public boolean isUnlocked(Unlockable u) {
        return unlockables.contains(u);
    }

    public void addAbilities(Set<Ability> abilities) {
        for (Unlockable u : unlockables)
            if (u instanceof Ability)
                abilities.add((Ability) u);
    }

    public void levelUp() {
        level++;
        if (level % ConfigHandler.skillPointInterval == 0)
            skillPoints++;
    }

    public void unlock(Unlockable u) {
        skillPoints -= u.cost;
        unlockables.add(u);
    }

    public void respec() {
        unlockables.clear();
        skillPoints = level / ConfigHandler.skillPointInterval;
    }

    public void forEachEventHandler(Consumer<IAbilityEventHandler> consumer) {
        unlockables.forEach((u) -> {
            if (u instanceof IAbilityEventHandler)
                consumer.accept((IAbilityEventHandler) u);
        });
    }

}
