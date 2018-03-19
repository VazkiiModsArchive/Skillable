package codersafterdark.reskillable.api;

import codersafterdark.reskillable.api.skill.SkillConfig;
import codersafterdark.reskillable.api.unlockable.UnlockableConfig;
import net.minecraft.util.ResourceLocation;

public interface IModAccess {
    SkillConfig getSkillConfig(ResourceLocation name);

    UnlockableConfig getUnlockableConfig(ResourceLocation name, int cost, String[] defaultRequirements);
}
