package vazkii.skillable.skill.magic;

import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import vazkii.skillable.skill.base.Trait;

public class TraitSafePort extends Trait {

    public TraitSafePort() {
        super("safe_port", 1, 1, 6, "magic:20,agility:16,defense:16");
    }

    @Override
    public void onEnderTeleport(EnderTeleportEvent event) {
        event.setAttackDamage(0);
    }

}
