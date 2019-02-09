package codersafterdark.reskillable.api.data;

import codersafterdark.reskillable.api.ReskillableAPI;
import codersafterdark.reskillable.api.requirement.NoneRequirement;
import codersafterdark.reskillable.api.requirement.Requirement;
import codersafterdark.reskillable.api.requirement.RequirementComparision;
import codersafterdark.reskillable.api.requirement.logic.TrueRequirement;
import codersafterdark.reskillable.base.ConfigHandler;
import codersafterdark.reskillable.lib.LibObfuscation;
import com.google.common.collect.Lists;
import net.minecraft.advancements.AdvancementList;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class RequirementHolder {
    private static AdvancementList advList;
    private final List<Requirement> requirements;
    private final boolean forcedEmpty;
    private boolean hasNone;

    public RequirementHolder() {
        this.requirements = Lists.newArrayList();
        this.forcedEmpty = true;
    }

    public RequirementHolder(List<Requirement> requirements) {
        this.requirements = requirements;
        this.forcedEmpty = false;
    }

    public RequirementHolder(RequirementHolder... others) {
        this.requirements = Lists.newArrayList();
        this.forcedEmpty = false;
        for (RequirementHolder other : others) {
            if (other.hasNone) {
                this.requirements.addAll(other.requirements);
                hasNone = true;
                break;
            }
            other.requirements.forEach(otherRequirement -> addRequirement(this.requirements, otherRequirement));
        }
    }

    public static RequirementHolder noneHolder() {
        RequirementHolder requirementHolder = new RequirementHolder(new ArrayList<>());
        requirementHolder.hasNone = true;
        requirementHolder.requirements.add(new NoneRequirement());
        return requirementHolder;
    }

    public static RequirementHolder realEmpty() {
        return new RequirementHolder();
    }

    public static RequirementHolder fromStringList(String[] requirementStringList) {
        //TODO If length is 1 try splitting the string. Instead it is probably better to follow the TODO below for deprecating single string requirement lists
        List<Requirement> requirements = new ArrayList<>();
        for (String s : requirementStringList) {
            Requirement requirement = ReskillableAPI.getInstance().getRequirementRegistry().getRequirement(s);
            if (requirement instanceof NoneRequirement) {
                return noneHolder();
            }
            addRequirement(requirements, requirement);
        }
        return requirements.isEmpty() ? RequirementHolder.realEmpty() : new RequirementHolder(requirements);
    }

    //TODO: Rewrite config system to store things in a format closer to JSON so that requirements are as a list which would remove the need for this
    //TODO Cont: This would make sure that there are no issues storing things like ItemRequirements if they have NBT data with commas in it
    public static RequirementHolder fromString(String s) {
        RequirementHolder requirementHolder;
        if (s.matches("(?i)^(null|nil)$")) {
            requirementHolder = RequirementHolder.realEmpty();
        } else {
            requirementHolder = fromStringList(s.split(","));
        }

        return requirementHolder;
    }

    private static void addRequirement(List<Requirement> requirements, Requirement requirement) {
        if (requirement == null || requirement instanceof TrueRequirement) {
            return;
        }
        for (int i = 0; i < requirements.size(); i++) {
            RequirementComparision match = requirements.get(i).matches(requirement);
            if (match.equals(RequirementComparision.EQUAL_TO) || match.equals(RequirementComparision.GREATER_THAN)) {
                return;
            } else if (match.equals(RequirementComparision.LESS_THAN)) {
                requirements.remove(i);
                break;
            }
        }
        requirements.add(requirement);
    }

    public static AdvancementList getAdvancementList() {
        if (advList == null) {
            advList = ReflectionHelper.getPrivateValue(AdvancementManager.class, null, LibObfuscation.ADVANCEMENT_LIST);
        }
        return advList;
    }

    public boolean isRealLock() {
        return getRestrictionLength() > 0 && !forcedEmpty;
    }

    public boolean isForcedEmpty() {
        return forcedEmpty;
    }

    public int getRestrictionLength() {
        return requirements.size();
    }

    @SideOnly(Side.CLIENT)
    public void addRequirementsToTooltip(PlayerData data, List<String> tooltip) {
        if (!isRealLock()) {
            return;
        }
        if (!ConfigHandler.hideRequirements || GuiScreen.isShiftKeyDown()) {
            tooltip.add(TextFormatting.DARK_PURPLE + new TextComponentTranslation("reskillable.misc.requirements").getUnformattedComponentText());
            addRequirementsIgnoreShift(data, tooltip);
        } else {
            tooltip.add(TextFormatting.DARK_PURPLE + new TextComponentTranslation("reskillable.misc.requirements_shift").getUnformattedComponentText());
        }
    }

    @SideOnly(Side.CLIENT)
    public void addRequirementsIgnoreShift(PlayerData data, List<String> tooltip) {
        if (isRealLock()) {
            requirements.stream().map(requirement -> requirement.getToolTip(data)).forEach(tooltip::add);
        }
    }

    public List<Requirement> getRequirements() {
        return requirements;
    }

    public boolean hasNone() {
        return hasNone;
    }
}