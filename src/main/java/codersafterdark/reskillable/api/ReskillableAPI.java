package codersafterdark.reskillable.api;

import codersafterdark.reskillable.api.skill.SkillConfig;
import codersafterdark.reskillable.api.unlockable.UnlockableConfig;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;

public class ReskillableAPI {
    private static ReskillableAPI instance;
    private IModAccess modAccess;

    public ReskillableAPI(IModAccess modAccess) {
        this.modAccess = modAccess;
    }

    public SkillConfig getSkillConfig(ResourceLocation name) {
        return modAccess.getSkillConfig(name);
    }

    public static ReskillableAPI getInstance() {
        return Objects.requireNonNull(instance, "Calling Reskillable API Instance before it's creation");
    }

    public static void setInstance(ReskillableAPI reskillableAPI) {
        instance = reskillableAPI;
    }

    public UnlockableConfig getTraitConfig(ResourceLocation name, int cost, String[] defaultRequirements) {
        return modAccess.getUnlockableConfig(name, cost, defaultRequirements);
    }
}
