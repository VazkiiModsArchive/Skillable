package codersafterdark.reskillable.api;

import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.api.unlockable.Unlockable;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class ReskillableRegistries {
    public final static IForgeRegistry<Skill> SKILLS = GameRegistry.findRegistry(Skill.class);
    public final static IForgeRegistry<Unlockable> UNLOCKABLES = GameRegistry.findRegistry(Unlockable.class);
}