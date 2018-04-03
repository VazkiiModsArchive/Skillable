package codersafterdark.reskillable.api.unlockable;

import net.minecraft.util.ResourceLocation;

public abstract class Trait extends Unlockable implements IAbilityEventHandler {
    public Trait(ResourceLocation name, int x, int y, ResourceLocation skillName, int cost, String... requirements) {
        super(name, x, y, skillName, cost, requirements);
    }
}
