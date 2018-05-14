package codersafterdark.reskillable.api.data;

import codersafterdark.reskillable.api.ReskillableRegistries;
import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.api.unlockable.Ability;
import codersafterdark.reskillable.api.unlockable.IAbilityEventHandler;
import codersafterdark.reskillable.api.unlockable.Unlockable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public class PlayerSkillInfo {

    private static final String TAG_LEVEL = "level";
    private static final String TAG_SKILL_POINTS = "skillPoints";
    private static final String TAG_UNLOCKABLES = "unlockables";

    public final Skill skill;

    private int level;
    private int skillPoints;
    private List<Unlockable> unlockables = new ArrayList<>();

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

        for (String s : unlockablesCmp.getKeySet()) {
            Optional.ofNullable(ReskillableRegistries.UNLOCKABLES.getValue(new ResourceLocation(s.replace(".", ":"))))
                    .ifPresent(unlockables::add);
        }
    }

    public void saveToNBT(NBTTagCompound cmp) {
        cmp.setInteger(TAG_LEVEL, level);
        cmp.setInteger(TAG_SKILL_POINTS, skillPoints);

        NBTTagCompound unlockablesCmp = new NBTTagCompound();
        for (Unlockable u : unlockables) {
            unlockablesCmp.setBoolean(u.getKey(), true);
        }
        cmp.setTag(TAG_UNLOCKABLES, unlockablesCmp);
    }

    public int getLevel() {
        if (level <= 0) {
            level = 1;
        }
        if (level > skill.getCap()) {
            level = skill.getCap();
        }

        return level;
    }

    public int getRank() {
        return 8 * level / skill.getCap();
    }

    public int getSkillPoints() {
        return skillPoints;
    }

    public boolean isCapped() {
        return level >= skill.getCap();
    }

    public int getLevelUpCost() {
        return skill.getLevelUpCost(level);
    }

    public boolean isUnlocked(Unlockable u) {
        return unlockables.contains(u);
    }

    public void addAbilities(Set<Ability> abilities) {
        for (Unlockable u : unlockables) {
            if (u instanceof Ability) {
                abilities.add((Ability) u);
            }
        }
    }

    //TODO decide if this should just call setLevel(level + 1);
    public void levelUp() {
        level++;
        if (level % skill.getSkillPointInterval() == 0) {
            skillPoints++;
        }
    }

    public void setLevel(int level) {
        int interval = skill.getSkillPointInterval();
        skillPoints += level / interval - this.level / interval;
        this.level = level;
    }

    public void lock(Unlockable u, EntityPlayer p) {
        skillPoints += u.getCost();
        unlockables.remove(u);
        u.onLock(p);
    }

    public void unlock(Unlockable u, EntityPlayer p) {
        skillPoints -= u.getCost();
        unlockables.add(u);
        u.onUnlock(p);
    }

    public void respec() {
        unlockables.clear();
        skillPoints = level / skill.getSkillPointInterval();
    }

    public void forEachEventHandler(Consumer<IAbilityEventHandler> consumer) {
        unlockables.forEach((u) -> {
            if (u.isEnabled() && u instanceof IAbilityEventHandler) {
                consumer.accept((IAbilityEventHandler) u);
            }
        });
    }
}
