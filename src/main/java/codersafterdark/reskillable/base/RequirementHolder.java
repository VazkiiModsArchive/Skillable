package codersafterdark.reskillable.base;

import codersafterdark.reskillable.api.ReskillableRegistries;
import codersafterdark.reskillable.api.requirement.AdvancementRequirement;
import codersafterdark.reskillable.api.requirement.Requirement;
import codersafterdark.reskillable.api.requirement.SkillRequirement;
import codersafterdark.reskillable.lib.LibObfuscation;
import codersafterdark.reskillable.api.skill.Skill;
import com.google.common.collect.Lists;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementList;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static RequirementHolder realEmpty() {
        return new RequirementHolder();
    }

    public static RequirementHolder fromString(String s) {
        if (s.matches("(?i)^(none|null|nil)$"))
            return RequirementHolder.realEmpty();

        List<Requirement> requirements = new ArrayList<>();
        String[] tokens = s.trim().split(",");

        for (String s1 : tokens) {
            String[] kv = s1.split("\\|");
            if (kv.length == 2) {
                String keyStr = kv[0];
                String valStr = kv[1];

                if (keyStr.equals("adv"))
                    requirements.add(new AdvancementRequirement(new ResourceLocation(valStr)));
                else
                    try {
                        int level = Integer.parseInt(valStr);
                        Skill skill = ReskillableRegistries.SKILLS.getValue(new ResourceLocation(keyStr.toLowerCase()));
                        if (skill != null && level > 1)
                            requirements.add(new SkillRequirement(skill, level));
                        else
                            FMLLog.warning("[Reskillable] Invalid Level Lock: " + s);
                    } catch (NumberFormatException e) {
                        FMLLog.warning("[Reskillable] Invalid Level Lock: " + s);
                    }
            }
        }
        return new RequirementHolder(requirements);
    }

    public static AdvancementList getAdvancementList() {
        if (advList == null)
            advList = ReflectionHelper.getPrivateValue(AdvancementManager.class, null, LibObfuscation.ADVANCEMENT_LIST);

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
