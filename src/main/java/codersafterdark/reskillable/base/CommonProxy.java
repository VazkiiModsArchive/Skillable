package codersafterdark.reskillable.base;

import codersafterdark.reskillable.advancement.ReskillableAdvancements;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.requirement.RequirementCache;
import codersafterdark.reskillable.api.unlockable.AutoUnlocker;
import codersafterdark.reskillable.commands.ReskillableCmd;
import codersafterdark.reskillable.loot.LootConditionRequirement;
import codersafterdark.reskillable.network.PacketHandler;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(PlayerDataHandler.EventHandler.class);
        MinecraftForge.EVENT_BUS.register(LevelLockHandler.class);
        MinecraftForge.EVENT_BUS.register(RequirementCache.class);
        ConfigHandler.init(event.getSuggestedConfigurationFile());
        PacketHandler.preInit();
        LootConditionManager.registerCondition(new LootConditionRequirement.Serializer());
        ReskillableAdvancements.preInit();
    }

    public void init(FMLInitializationEvent event) {
        if (ConfigHandler.config.hasChanged()) {
            ConfigHandler.config.save();
        }
    }

    public void postInit(FMLPostInitializationEvent event) {
        LevelLockHandler.setupLocks();
        RequirementCache.registerDirtyTypes();
    }

    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new ReskillableCmd());
        AutoUnlocker.setUnlockables();
        MinecraftForge.EVENT_BUS.register(AutoUnlocker.class);
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