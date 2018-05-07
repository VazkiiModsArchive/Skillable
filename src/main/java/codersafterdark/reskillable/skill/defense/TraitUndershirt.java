package codersafterdark.reskillable.skill.defense;

import codersafterdark.reskillable.api.unlockable.Trait;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

import static codersafterdark.reskillable.lib.LibMisc.MOD_ID;

public class TraitUndershirt extends Trait {

    private static final String TAG_COOLDOWN = "skillable:UndershirtCD";

    public TraitUndershirt() {
        super(new ResourceLocation(MOD_ID, "undershirt"), 1, 2, new ResourceLocation(MOD_ID, "defense"),
                6, "reskillable:defense|12", "reskillable:agility|4");
    }

    @Override
    public void onHurt(LivingHurtEvent event) {
        if (event.isCanceled()) {
            return;
        }
        EntityLivingBase e = event.getEntityLiving();
        if (e.getEntityData().getInteger(TAG_COOLDOWN) == 0 && e.getHealth() >= 6 && event.getAmount() >= e.getHealth() && !event.getSource().isUnblockable()) {
            event.setAmount(e.getHealth() - 1);
            e.getEntityData().setInteger(TAG_COOLDOWN, 200);
        }
    }

    @Override
    public void onPlayerTick(PlayerTickEvent event) {
        int cd = event.player.getEntityData().getInteger(TAG_COOLDOWN);
        if (cd > 0) {
            event.player.getEntityData().setInteger(TAG_COOLDOWN, cd - 1);
        }
    }

}
