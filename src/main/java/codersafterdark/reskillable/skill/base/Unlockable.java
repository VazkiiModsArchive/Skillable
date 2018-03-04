package codersafterdark.reskillable.skill.base;

import codersafterdark.reskillable.base.ConfigHandler;
import codersafterdark.reskillable.base.RequirementHolder;
import codersafterdark.reskillable.lib.LibMisc;
import codersafterdark.reskillable.skill.Skill;
import codersafterdark.reskillable.skill.Skills;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

public abstract class Unlockable implements Comparable<Unlockable> {

    public final int x, y, cost;
    public final boolean enabled;

    private final String name;
    private final ResourceLocation icon;
    private final String configRequirements;
    private RequirementHolder requirements;
    private Skill parentSkill;

    public Unlockable(String name, int x, int y, int cost, String reqs) {
        this.name = name;
        this.x = x;
        this.y = y;

        String category = "traits." + name;
        enabled = ConfigHandler.config.get(category, "Enabled", true).getBoolean();
        this.cost = ConfigHandler.config.get(category, "Skill Points", cost).getInt();

        reqs = reqs.replaceAll("\\:", "|"); // backwards compatibility
        configRequirements = ConfigHandler.config.get(category, "Requirements", reqs).getString();
        this.requirements = null;


        icon = new ResourceLocation(LibMisc.MOD_ID, "textures/skills/" + name + ".png");

        if (enabled)
            Skills.ALL_UNLOCKABLES.put(name, this);
    }

    public Skill getParentSkill() {
        return parentSkill;
    }

    public void setParentSkill(Skill parentSkill) {
        this.parentSkill = parentSkill;
    }

    public RequirementHolder getRequirements() {
        if (requirements == null)
            requirements = RequirementHolder.fromString(configRequirements);

        return requirements;
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

    public boolean hasSpikes() {
        return false;
    }

    @Override
    public int compareTo(Unlockable o) {
        int skillCmp = getParentSkill().compareTo(o.getParentSkill());
        if (skillCmp == 0)
            return getName().compareTo(o.getName());

        return skillCmp;
    }

}
