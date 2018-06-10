package codersafterdark.reskillable.base;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.data.RequirementHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class ToolTipHandler {
    private static RequirementHolder lastLock = LevelLockHandler.EMPTY_LOCK;
    private static List<String> toolTip = new ArrayList<>();
    private static ItemStack lastItem;
    private static boolean enabled;

    public static void resetLast() {
        lastItem = null;
        lastLock = LevelLockHandler.EMPTY_LOCK;
        toolTip = new ArrayList<>();
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onTooltip(ItemTooltipEvent event) {
        if (!enabled || event.isCanceled()) {
            return;
        }
        ItemStack current = event.getItemStack();
        PlayerData data = PlayerDataHandler.get(Minecraft.getMinecraft().player);
        if (lastItem != current) {
            lastItem = current;
            lastLock = LevelLockHandler.getSkillLock(current);
            lastLock.addRequirementsIgnoreShift(data, toolTip = new ArrayList<>());
        }
        List<String> curTooltip = event.getToolTip();
        if (!toolTip.isEmpty()) {
            if (!ConfigHandler.hideRequirements || GuiScreen.isShiftKeyDown()) {
                curTooltip.add(TextFormatting.DARK_PURPLE + new TextComponentTranslation("skillable.misc.skillLock").getUnformattedComponentText());
                curTooltip.addAll(toolTip);
            } else {
                curTooltip.add(TextFormatting.DARK_PURPLE + new TextComponentTranslation("skillable.misc.skillLockShift").getUnformattedComponentText());
            }
        }
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