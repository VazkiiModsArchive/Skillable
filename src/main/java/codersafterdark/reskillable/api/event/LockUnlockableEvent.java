package codersafterdark.reskillable.api.event;

import codersafterdark.reskillable.api.unlockable.Unlockable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

public class LockUnlockableEvent extends PlayerEvent {
    private Unlockable unlockable;

    protected LockUnlockableEvent(EntityPlayer player, Unlockable unlockable) {
        super(player);
        this.unlockable = unlockable;
    }

    public Unlockable getUnlockable() {
        return unlockable;
    }

    @Cancelable
    public static class Pre extends LockUnlockableEvent {
        public Pre(EntityPlayer player, Unlockable unlockable) {
            super(player, unlockable);
        }
    }

    public static class Post extends LockUnlockableEvent {
        public Post(EntityPlayer player, Unlockable unlockable) {
            super(player, unlockable);
        }
    }
}
