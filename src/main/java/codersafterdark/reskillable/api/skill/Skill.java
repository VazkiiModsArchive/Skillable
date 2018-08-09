package codersafterdark.reskillable.api.skill;

import codersafterdark.reskillable.api.ReskillableAPI;
import codersafterdark.reskillable.api.unlockable.Unlockable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Skill extends IForgeRegistryEntry.Impl<Skill> implements Comparable<Skill> {
    private final Map<Integer, ResourceLocation> customSprites = new HashMap<>();
    private final List<Unlockable> unlockables = new ArrayList<>();
    private final ResourceLocation spriteLocation;
    private final String name;
    protected ResourceLocation background;
    protected SkillConfig skillConfig;
    private boolean hidden;

    public Skill(ResourceLocation name, ResourceLocation background) {
        this.name = name.toString().replace(":", ".");
        this.background = background;
        this.spriteLocation = new ResourceLocation(name.getResourceDomain(), "textures/skills/" + name.getResourcePath() + ".png");
        this.setRegistryName(name);
        this.skillConfig = ReskillableAPI.getInstance().getSkillConfig(name);
    }

    public void addUnlockable(Unlockable unlockable) {
        unlockables.add(unlockable);
    }

    public List<Unlockable> getUnlockables() {
        return unlockables;
    }

    public String getKey() {
        return name;
    }

    public String getName() {
        return new TextComponentTranslation("skillable.skill." + getKey()).getUnformattedComponentText();
    }

    public ResourceLocation getBackground() {
        return background;
    }

    public int getCap() {
        return skillConfig.getLevelCap();
    }

    public boolean isEnabled() {
        return skillConfig.isEnabled();
    }

    public ResourceLocation getSpriteLocation() {
        return spriteLocation;
    }

    public Pair<Integer, Integer> getSpriteFromRank(int rank) {
        //TODO: If we ever end up having more images than 4 when the Math.min is changed make sure to also change the value rank is divided by
        return new MutablePair<>(Math.min(rank / 2, 3) * 16, 0);
    }

    public void setCustomSprite(int rank, ResourceLocation location) {
        customSprites.put(rank, location);
    }

    public void removeCustomSprite(int rank) {
        customSprites.remove(rank);
    }

    public ResourceLocation getSpriteLocation(int rank) {
        if (customSprites.containsKey(rank)) {
            return customSprites.get(rank);
        }
        for (int i = rank - 1; i >= 0; i--) {
            if (customSprites.containsKey(i)) {
                return customSprites.get(i);
            }
        }
        return null;
    }

    public boolean hasCustomSprites() {
        return !customSprites.isEmpty();
    }

    @Override
    public int compareTo(@Nonnull Skill o) {
        return o.getName().compareTo(this.getName());
    }

    public int getSkillPointInterval() {
        return skillConfig.getSkillPointInterval();
    }

    public int getLevelUpCost(int level) {
        int cost = this.skillConfig.getLevelStaggering()
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey() < level + 1)
                .mapToInt(Map.Entry::getValue)
                .sum() + this.skillConfig.getBaseLevelCost();
        return cost < 0 ? 0 : cost;
    }

    public final SkillConfig getSkillConfig() {
        return skillConfig;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public int getRank(int level) {
        return 8 * level / getCap();
    }
}