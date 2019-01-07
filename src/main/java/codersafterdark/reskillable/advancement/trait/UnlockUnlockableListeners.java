package codersafterdark.reskillable.advancement.trait;

import codersafterdark.reskillable.advancement.CriterionListeners;
import codersafterdark.reskillable.api.unlockable.Trait;
import codersafterdark.reskillable.api.unlockable.Unlockable;
import net.minecraft.advancements.PlayerAdvancements;

public class UnlockUnlockableListeners extends CriterionListeners<UnlockUnlockableCriterionInstance> {
    public UnlockUnlockableListeners(PlayerAdvancements playerAdvancements) {
        super(playerAdvancements);
    }

    public void trigger(final Unlockable unlockable) {
        trigger(instance -> instance.test(unlockable));
    }
}
