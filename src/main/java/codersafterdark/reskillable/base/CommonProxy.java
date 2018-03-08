package codersafterdark.reskillable.base;

import codersafterdark.reskillable.event.RegisterUnlockablesEvent;
import codersafterdark.reskillable.network.MessageRegister;
import codersafterdark.reskillable.skill.Skills;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(RegisterUnlockablesEvent.class);
        Skills.init();
        MinecraftForge.EVENT_BUS.register(PlayerDataHandler.EventHandler.class);
        MinecraftForge.EVENT_BUS.register(LevelLockHandler.class);
        ConfigHandler.init(event.getSuggestedConfigurationFile());
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
