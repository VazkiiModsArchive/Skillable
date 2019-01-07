package codersafterdark.reskillable.advancement;

import codersafterdark.reskillable.advancement.skilllevel.SkillLevelTrigger;
import codersafterdark.reskillable.advancement.trait.UnlockUnlockableTrigger;
import net.minecraft.advancements.CriteriaTriggers;

public class ReskillableAdvancements {
    public static final SkillLevelTrigger SKILL_LEVEL = new SkillLevelTrigger();
    public static final UnlockUnlockableTrigger UNLOCK_UNLOCKABLE = new UnlockUnlockableTrigger();

    public static void preInit() {
        CriteriaTriggers.register(SKILL_LEVEL);
        CriteriaTriggers.register(UNLOCK_UNLOCKABLE);
    }
}
