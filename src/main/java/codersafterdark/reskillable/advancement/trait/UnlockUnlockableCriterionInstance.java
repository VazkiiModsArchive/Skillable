package codersafterdark.reskillable.advancement.trait;

import codersafterdark.reskillable.api.unlockable.Unlockable;
import codersafterdark.reskillable.lib.LibMisc;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.util.ResourceLocation;

public class UnlockUnlockableCriterionInstance extends AbstractCriterionInstance {
    private final Unlockable unlockable;

    public UnlockUnlockableCriterionInstance(Unlockable unlockable) {
        super(new ResourceLocation(LibMisc.MOD_ID, "unlockable"));
        this.unlockable = unlockable;
    }

    public boolean test(Unlockable other) {
        return unlockable == other;
    }
}
