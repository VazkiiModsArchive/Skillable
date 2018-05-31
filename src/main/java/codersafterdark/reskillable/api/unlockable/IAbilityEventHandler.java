package codersafterdark.reskillable.api.unlockable;

import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public interface IAbilityEventHandler {

    default void onPlayerTick(PlayerTickEvent event) {
    }

    default void onBlockDrops(HarvestDropsEvent event) {
    }

    default void getBreakSpeed(BreakSpeed event) {
    }

    default void onMobDrops(LivingDropsEvent event) {
    }

    default void onAttackMob(LivingHurtEvent event) {
    }

    default void onHurt(LivingHurtEvent event) {
    }

    default void onRightClickBlock(RightClickBlock event) {
    }

    default void onEnderTeleport(EnderTeleportEvent event) {
    }

    default void onKillMob(LivingDeathEvent event) {
    }

}
