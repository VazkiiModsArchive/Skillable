package codersafterdark.reskillable.api.data;

import codersafterdark.reskillable.Reskillable;
import codersafterdark.reskillable.api.ReskillableAPI;
import codersafterdark.reskillable.api.ReskillableRegistries;
import codersafterdark.reskillable.api.requirement.AdvancementRequirement;
import codersafterdark.reskillable.api.requirement.Requirement;
import codersafterdark.reskillable.api.requirement.SkillRequirement;
import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.api.unlockable.Ability;
import codersafterdark.reskillable.api.requirement.TraitRequirement;
import codersafterdark.reskillable.api.unlockable.Unlockable;
import codersafterdark.reskillable.lib.LibObfuscation;
import com.google.common.collect.Lists;
import net.minecraft.advancements.AdvancementList;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RequirementHolder {

    private static AdvancementList advList;
    private static PlayerData playerData;
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

    public static RequirementHolder realEmpty() {
        return new RequirementHolder();
    }

    public static RequirementHolder fromStringList(String[] requirementStringList) {
        RequirementHolder requirementHolder;

        if (requirementStringList.length == 0) {
            requirementHolder = RequirementHolder.realEmpty();
        } else {
            List<Requirement> requirements = Arrays.stream(requirementStringList)
                    .map(ReskillableAPI.getInstance().getRequirementRegistry()::getRequirement)
                    .collect(Collectors.toList());
            requirementHolder = new RequirementHolder(requirements);
        }
        return requirementHolder;
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
        if (!isRealLock())
            return;

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
