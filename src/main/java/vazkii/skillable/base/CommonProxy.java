package vazkii.skillable.base;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;
import vazkii.arl.network.NetworkHandler;
import vazkii.skillable.network.MessageDataSync;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new PlayerDataHandler.EventHandler());
		
		NetworkHandler.register(MessageDataSync.class, Side.CLIENT);
	}

	public void init(FMLInitializationEvent event) {

	}

	public void postInit(FMLPostInitializationEvent event) {

	}
	
	public void serverStarting(FMLServerStartingEvent event) {

	}
	
	public EntityPlayer getClientPlayer() {
		return null;
	}
	
}
