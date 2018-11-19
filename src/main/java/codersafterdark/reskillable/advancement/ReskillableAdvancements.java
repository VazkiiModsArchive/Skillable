package codersafterdark.reskillable.advancement;

import codersafterdark.reskillable.advancement.skilllevel.SkillLevelTrigger;
import net.minecraft.advancements.CriteriaTriggers;

public class ReskillableAdvancements {
    public static final SkillLevelTrigger SKILL_LEVEL = new SkillLevelTrigger();

    public static void preInit() {
        CriteriaTriggers.register(SKILL_LEVEL);
    }
}
