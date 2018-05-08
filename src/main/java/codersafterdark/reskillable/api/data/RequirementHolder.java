package codersafterdark.reskillable.api.data;

import codersafterdark.reskillable.api.ReskillableAPI;
import codersafterdark.reskillable.api.requirement.Requirement;
import codersafterdark.reskillable.api.requirement.RequirementComparision;
import codersafterdark.reskillable.lib.LibObfuscation;
import com.google.common.collect.Lists;
import net.minecraft.advancements.AdvancementList;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class RequirementHolder {

    private static AdvancementList advList;
    private final List<Requirement> requirements;
    private final boolean forcedEmpty;

    public RequirementHolder() {
        this.requirements = Lists.newArrayList();
        this.forcedEmpty = true;
    }

    public RequirementHolder(List<Requirement> requirements) {
        this.requirements = requirements;
        this.forcedEmpty = false;
    }

    public RequirementHolder(RequirementHolder... others) {
        this.forcedEmpty = false;
        this.requirements = Lists.newArrayList();
        //TODO Eventually look into optimizing this. Potentially by presorting others by requirement count
        for (RequirementHolder other : others) {
            for (Requirement otherRequirement : other.requirements) {
                boolean noMatch = true;
                int toRemove = -1;
                for (int i = 0; i < requirements.size(); i++) {
                    RequirementComparision match = requirements.get(i).matches(otherRequirement);
                    if (match.equals(RequirementComparision.EQUAL_TO) || match.equals(RequirementComparision.GREATER_THAN)) {
                        noMatch = false;
                        break;
                    } else if (match.equals(RequirementComparision.LESS_THAN)) {
                        toRemove = i;
                        break;
                    }
                }
                if (toRemove >= 0) {
                    requirements.remove(toRemove);
                }
                if (noMatch) {
                    requirements.add(otherRequirement);
                }
            }
        }
    }

    public static RequirementHolder realEmpty() {
        return new RequirementHolder();
    }

    public static RequirementHolder fromStringList(String[] requirementStringList) {
        List<Requirement> requirements = new ArrayList<>();
        for (String s : requirementStringList) {
            Requirement requirement = ReskillableAPI.getInstance().getRequirementRegistry().getRequirement(s);
            if (requirement != null) {
                requirements.add(requirement);
            }
        }
        return requirements.isEmpty() ? RequirementHolder.realEmpty() : new RequirementHolder(requirements);
    }

    public static RequirementHolder fromString(String s) {
        RequirementHolder requirementHolder;
        if (s.matches("(?i)^(none|null|nil)$")) {
            requirementHolder = RequirementHolder.realEmpty();
        } else {
            requirementHolder = fromStringList(s.split(","));
        }

        return requirementHolder;
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
        if (GuiScreen.isShiftKeyDown()) {
            tooltip.add(TextFormatting.DARK_PURPLE + I18n.translateToLocal("skillable.misc.skillLock"));
            for (Requirement requirement : requirements) {
                tooltip.add(requirement.getToolTip(data));
            }
        } else {
            tooltip.add(TextFormatting.DARK_PURPLE + I18n.translateToLocal("skillable.misc.skillLockShift"));
        }
    }

    public List<Requirement> getRequirements() {
        return requirements;
    }
}
