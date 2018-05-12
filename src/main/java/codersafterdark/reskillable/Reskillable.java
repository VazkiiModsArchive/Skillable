package codersafterdark.reskillable;

import codersafterdark.reskillable.api.ReskillableAPI;
import codersafterdark.reskillable.base.CommonProxy;
import codersafterdark.reskillable.base.ConfigHandler;
import codersafterdark.reskillable.commands.ReskillableCmd;
import codersafterdark.reskillable.lib.LibMisc;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;

import static codersafterdark.reskillable.lib.LibMisc.MOD_ID;

@Mod(modid = MOD_ID, name = LibMisc.MOD_NAME, version = LibMisc.VERSION, guiFactory = LibMisc.GUI_FACTORY)
public class Reskillable {


    @SidedProxy(serverSide = LibMisc.PROXY_COMMON, clientSide = LibMisc.PROXY_CLIENT)
    public static CommonProxy proxy;

    public static Logger logger;

    public Reskillable() {
        ReskillableAPI.setInstance(new ReskillableAPI(new ReskillableModAccess()));
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        if (ConfigHandler.config.hasChanged()) {
            ConfigHandler.config.save();
        }

        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
        proxy.registerKeyBindings();
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new ReskillableCmd());

        proxy.serverStarting(event);
    }
}
