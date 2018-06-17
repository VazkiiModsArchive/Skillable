package codersafterdark.reskillable.api.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

public class CacheInvalidatedEvent extends Event {
    private EntityPlayer player;

    public CacheInvalidatedEvent(EntityPlayer player) {
        this.player = player;
    }

    /**
     * @return The player who's cache was invalidated.
     */
    public EntityPlayer getPlayer() {
        return this.player;
    }
}