package codersafterdark.reskillable.skill.magic;

import codersafterdark.reskillable.api.unlockable.Trait;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;

import static codersafterdark.reskillable.lib.LibMisc.MOD_ID;

public class TraitSafePort extends Trait {

    public TraitSafePort() {
        super(new ResourceLocation(MOD_ID, "safe_port"), 1, 1, new ResourceLocation(MOD_ID, "magic"),
                6, "reskillable:magic|20", "reskillable:agility|16", "reskillable:defense|16");
    }

    @Override
    public void onEnderTeleport(EnderTeleportEvent event) {
        if (!event.isCanceled()) {
            event.setAttackDamage(0);
        }
    }

}
