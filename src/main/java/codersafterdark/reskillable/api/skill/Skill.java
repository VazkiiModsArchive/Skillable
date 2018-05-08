package codersafterdark.reskillable.api.skill;

import codersafterdark.reskillable.api.ReskillableAPI;
import codersafterdark.reskillable.api.unlockable.Unlockable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Skill extends IForgeRegistryEntry.Impl<Skill> implements Comparable<Skill> {
    private final ResourceLocation spriteLocation;
    private final String name;
    private final List<Unlockable> unlockables = new ArrayList<>();
    protected ResourceLocation background;
    protected SkillConfig skillConfig;

    public Skill(ResourceLocation name, ResourceLocation background) {
        this.name = name.toString().replace(":", ".");
        this.background = background;
        this.spriteLocation = new ResourceLocation(name.getResourceDomain(), "textures/skills/" +
                name.getResourcePath() + ".png");
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
        return I18n.translateToLocal("skillable.skill." + getKey());
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

    @Override
    public int compareTo(@Nonnull Skill o) {
        return o.getName().compareTo(this.getName());
    }

    public int getSkillPointInterval() {
        return skillConfig.getSkillPointInterval();
    }

    public int getLevelUpCost(int level) {
        return this.skillConfig.getLevelStaggering()
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey() < level + 1)
                .mapToInt(Map.Entry::getValue)
                .sum() + this.skillConfig.getBaseLevelCost();
    }
}
