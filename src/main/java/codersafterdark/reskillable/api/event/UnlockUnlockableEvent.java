package codersafterdark.reskillable.api.event;

import codersafterdark.reskillable.api.unlockable.Unlockable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

public class UnlockUnlockableEvent extends PlayerEvent {
    private Unlockable unlockable;

    protected UnlockUnlockableEvent(EntityPlayer player, Unlockable unlockable) {
        super(player);
        this.unlockable = unlockable;
    }

    public Unlockable getUnlockable() {
        return unlockable;
    }

    @Cancelable
    public static class Pre extends UnlockUnlockableEvent {
        public Pre(EntityPlayer player, Unlockable unlockable) {
            super(player, unlockable);
        }
    }

    public static class Post extends UnlockUnlockableEvent {
        public Post(EntityPlayer player, Unlockable unlockable) {
            super(player, unlockable);
        }
    }
}
