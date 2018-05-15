package codersafterdark.reskillable.base;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.data.RequirementHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ToolTipHandler {
    private static RequirementHolder lastLock = LevelLockHandler.EMPTY_LOCK;
    private static ItemStack lastItem;
    private static boolean enabled;

    public static void resetLast() {
        lastItem = null;
        lastLock = LevelLockHandler.EMPTY_LOCK;
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onTooltip(ItemTooltipEvent event) {
        if (!enabled || event.isCanceled()) {
            return;
        }
        ItemStack current = event.getItemStack();
        if (lastItem != current) {
            lastItem = current;
            lastLock = LevelLockHandler.getSkillLock(current);
        }
        PlayerData data = PlayerDataHandler.get(Minecraft.getMinecraft().player);
        lastLock.addRequirementsToTooltip(data, event.getToolTip());
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void connect(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        enabled = true;
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void disconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        enabled = false;
    }
}