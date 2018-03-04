package codersafterdark.reskillable.skill.magic;

import codersafterdark.reskillable.skill.base.Trait;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;

public class TraitSafePort extends Trait {

    public TraitSafePort() {
        super("safe_port", 1, 1, 6, "magic:20,agility:16,defense:16");
    }

    @Override
    public void onEnderTeleport(EnderTeleportEvent event) {
        event.setAttackDamage(0);
    }

}
