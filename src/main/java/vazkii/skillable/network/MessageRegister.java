package vazkii.skillable.network;

import net.minecraftforge.fml.relauncher.Side;
import vazkii.arl.network.NetworkHandler;

public class MessageRegister {
	
	public static void init() {
		NetworkHandler.register(MessageDataSync.class, Side.CLIENT);
	}

}
