package vazkii.skillable.skill.base;

import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public interface IAbilityEventHandler {
	
	public default void onPlayerTick(PlayerTickEvent event) { }
	public default void onBlockDrops(HarvestDropsEvent event) { }
	public default void getBreakSpeed(BreakSpeed event) { }
	
}
