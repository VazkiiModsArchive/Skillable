package vazkii.skillable.base;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import vazkii.skillable.network.MessageRegister;
import vazkii.skillable.skill.Skills;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        Skills.init();
        ConfigHandler.init(event.getSuggestedConfigurationFile());
        MinecraftForge.EVENT_BUS.register(PlayerDataHandler.EventHandler.class);
        MinecraftForge.EVENT_BUS.register(LevelLockHandler.class);
        MessageRegister.init();
    }

    public void init(FMLInitializationEvent event) {

    }

    public void postInit(FMLPostInitializationEvent event) {
        LevelLockHandler.setupLocks();
    }

    public void serverStarting(FMLServerStartingEvent event) {

    }

    public EntityPlayer getClientPlayer() {
        return null;
    }

}
