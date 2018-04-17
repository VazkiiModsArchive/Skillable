package codersafterdark.reskillable.api.unlockable;

import codersafterdark.reskillable.api.ReskillableAPI;
import codersafterdark.reskillable.api.ReskillableRegistries;
import codersafterdark.reskillable.api.data.RequirementHolder;
import codersafterdark.reskillable.api.skill.Skill;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import java.util.Objects;

public abstract class Unlockable extends IForgeRegistryEntry.Impl<Unlockable> implements Comparable<Unlockable> {

    public final int x;
    public final int y;
    private final String name;
    private final ResourceLocation icon;
    private Skill parentSkill;
    private ResourceLocation skillName;
    private UnlockableConfig unlockableConfig;

    public Unlockable(ResourceLocation name, int x, int y, ResourceLocation skillName, int cost, String... defaultRequirements) {
        this.name = name.toString().replace(":", ".");
        this.x = x;
        this.y = y;
        this.skillName = skillName;
        setRegistryName(name);
        icon = new ResourceLocation(name.getResourceDomain(), "textures/unlockables/" + name.getResourcePath() + ".png");
        this.unlockableConfig = ReskillableAPI.getInstance().getTraitConfig(name, x, y, cost, defaultRequirements);
    }

    @Nonnull
    public Skill getParentSkill() {
        if (parentSkill == null) {
            parentSkill = Objects.requireNonNull(ReskillableRegistries.SKILLS.getValue(skillName));
            parentSkill.addUnlockable(this);
        }
        return parentSkill;
    }

    public RequirementHolder getRequirements() {
        return unlockableConfig.getRequirementHolder();
    }

    public String getKey() {
        return name;
    }

    public String getName() {
        return I18n.translateToLocal("skillable.unlock." + getKey());
    }

    public String getDescription() {
        return I18n.translateToLocal("skillable.unlock." + getKey() + ".desc");
    }

    public ResourceLocation getIcon() {
        return icon;
    }

    public void onUnlock(EntityPlayer player) {
    }

    public boolean hasSpikes() {
        return false;
    }

    public boolean isEnabled() {
        return unlockableConfig.isEnabled();
    }

    @Override
    public int compareTo(@Nonnull Unlockable o) {
        int skillCmp = getParentSkill().compareTo(o.getParentSkill());
        if (skillCmp == 0) {
            return getName().compareTo(o.getName());
        }

        return skillCmp;
    }

    public int getCost() {
        return unlockableConfig.getCost();
    }

    public int getX() {
        return unlockableConfig.getX();
    }

    public int getY() {
        return unlockableConfig.getY();
    }

}
