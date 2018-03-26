package codersafterdark.reskillable.api.data;

import codersafterdark.reskillable.Reskillable;
import codersafterdark.reskillable.api.ReskillableRegistries;
import codersafterdark.reskillable.api.requirement.AdvancementRequirement;
import codersafterdark.reskillable.api.requirement.Requirement;
import codersafterdark.reskillable.api.requirement.SkillRequirement;
import codersafterdark.reskillable.api.requirement.TraitRequirement;
import codersafterdark.reskillable.api.skill.Skill;
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
import java.util.List;

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

    public static RequirementHolder fromStringList(String[] list) {
        RequirementHolder requirementHolder;

        if (list.length == 0) {
            requirementHolder = RequirementHolder.realEmpty();
        } else {
            List<Requirement> requirements = new ArrayList<>();
            for (String s1 : list) {
                String[] kv = s1.split("\\|");
                if (kv.length == 2) {
                    String keyStr = kv[0];
                    String valStr = kv[1];

                    if (keyStr.equals("adv")) {
                        requirements.add(new AdvancementRequirement(new ResourceLocation(valStr)));
                    } else if (keyStr.equals("trait")) {
                        requirements.add(new TraitRequirement(new ResourceLocation(valStr)));
                    } else {
                        try {
                            int level = Integer.parseInt(valStr);
                            Skill skill = ReskillableRegistries.SKILLS.getValue(new ResourceLocation(keyStr.toLowerCase()));
                            if (skill != null && level > 1) {
                                requirements.add(new SkillRequirement(skill, level));
                            } else {
                                Reskillable.logger.log(Level.WARN, "Invalid Level Lock: " + s1);
                            }
                        } catch (NumberFormatException e) {
                            Reskillable.logger.log(Level.WARN, "Invalid Level Lock: " + s1);
                        }
                    }
                } else {
                    Reskillable.logger.log(Level.WARN, "Invalid Level Lock: " + s1);
                }
            }
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
