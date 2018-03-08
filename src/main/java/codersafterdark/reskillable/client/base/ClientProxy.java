package codersafterdark.reskillable.client.base;

import codersafterdark.reskillable.base.CommonProxy;
import codersafterdark.reskillable.client.gui.handler.InventoryTabHandler;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        MinecraftForge.EVENT_BUS.register(ClientTickHandler.class);
        MinecraftForge.EVENT_BUS.register(InventoryTabHandler.class);
        MinecraftForge.EVENT_BUS.register(HUDHandler.class);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
        new AdvancementManager(null);
    }

    @Override
    public EntityPlayer getClientPlayer() {
        return Minecraft.getMinecraft().player;
    }

}
