package codersafterdark.reskillable.base;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.network.PacketHandler;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(PlayerDataHandler.EventHandler.class);
        MinecraftForge.EVENT_BUS.register(LevelLockHandler.class);
        ConfigHandler.init(event.getSuggestedConfigurationFile());
        PacketHandler.preInit();
    }

    public void init(FMLInitializationEvent event) {
    }

    public void postInit(FMLPostInitializationEvent event) {
        LevelLockHandler.setupLocks();
    }

    public void serverStarting(FMLServerStartingEvent event) {

    }

    public void registerKeyBindings() {

    }

    public AdvancementProgress getPlayerAdvancementProgress(EntityPlayer entityPlayer, Advancement advancement) {
        return ((EntityPlayerMP) entityPlayer).getAdvancements().getProgress(advancement);
    }

    public EntityPlayer getClientPlayer() {
        return null;
    }

    public String getLocalizedString(String string) {
        return string;
    }
}
