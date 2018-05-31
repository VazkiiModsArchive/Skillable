package codersafterdark.reskillable.api.unlockable;

import net.minecraft.util.ResourceLocation;

public abstract class Ability extends Unlockable {
    public Ability(ResourceLocation name, int x, int y, ResourceLocation skillName, int cost, String... requirements) {
        super(name, x, y, skillName, cost, requirements);
    }

    @Override
    public boolean hasSpikes() {
        return true;
    }
}