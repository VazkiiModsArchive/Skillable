package vazkii.skillable.network;

import net.minecraftforge.fml.relauncher.Side;
import vazkii.arl.network.NetworkHandler;

public class MessageRegister {
	
	public static void init() {
		NetworkHandler.register(MessageDataSync.class, Side.CLIENT);
		NetworkHandler.register(MessageLevelUp.class, Side.SERVER);
		NetworkHandler.register(MessageUnlockUnlockable.class, Side.SERVER);
		NetworkHandler.register(MessageLockedItem.class, Side.CLIENT);
	}

}
