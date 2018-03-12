package codersafterdark.reskillable.skill.mining;

import codersafterdark.reskillable.api.unlockable.Ability;
import net.minecraft.util.ResourceLocation;

import static codersafterdark.reskillable.lib.LibMisc.MOD_ID;

public class AbilityOreCascade extends Ability {

    public AbilityOreCascade() {
        super(new ResourceLocation(MOD_ID,"ore_cascade"), 0, 3, new ResourceLocation(MOD_ID, "mining"), 6, "mining:12");
    }

}
