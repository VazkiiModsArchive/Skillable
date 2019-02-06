package codersafterdark.reskillable.api.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

public class CacheInvalidatedEvent extends Event {
    private EntityPlayer player;
    private boolean modified;

    public CacheInvalidatedEvent(EntityPlayer player) {
        this(player, false);
    }

    public CacheInvalidatedEvent(EntityPlayer player, boolean modified) {
        this.player = player;
        this.modified = modified;
    }

    /**
     * @return The player who's cache was invalidated.
     */
    public EntityPlayer getPlayer() {
        return this.player;
    }

    /**
     * @return True if any part of the cache was modified.
     */
    public boolean anyModified() {
        return modified;
    }
}