package codersafterdark.reskillable.client.gui.handler;

import codersafterdark.reskillable.base.ConfigHandler;
import codersafterdark.reskillable.client.base.RenderHelper;
import codersafterdark.reskillable.client.gui.GuiAbilities;
import codersafterdark.reskillable.client.gui.GuiSkillInfo;
import codersafterdark.reskillable.client.gui.GuiSkills;
import codersafterdark.reskillable.client.gui.button.GuiButtonInventoryTab;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.actors.threadpool.Arrays;

import java.util.List;

public class InventoryTabHandler {

    public static String tooltip;
    public static int mx, my;

    public static void addTabs(GuiScreen currScreen, List<GuiButton> buttonList) {
        if (!ConfigHandler.enableTabs) {
            return;
        }

        int x = currScreen.width / 2 - 120;
        int y = currScreen.height / 2 - 76;

        if (currScreen instanceof GuiContainerCreative) {
            x -= 10;
            y += 15;
        }
        buttonList.add(new GuiButtonInventoryTab(82931, x, y, GuiButtonInventoryTab.TabType.INVENTORY, (gui) -> gui instanceof GuiInventory || gui instanceof GuiContainerCreative));
        buttonList.add(new GuiButtonInventoryTab(82932, x, y + 29, GuiButtonInventoryTab.TabType.SKILLS, (gui) -> gui instanceof GuiSkills || gui instanceof GuiSkillInfo));
        buttonList.add(new GuiButtonInventoryTab(82933, x, y + 58, GuiButtonInventoryTab.TabType.ABILITIES, (gui) -> gui instanceof GuiAbilities));
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void initGui(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.getGui() instanceof GuiInventory || event.getGui() instanceof GuiContainerCreative) {
            addTabs(event.getGui(), event.getButtonList());
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void performAction(GuiScreenEvent.ActionPerformedEvent.Pre event) {
        if (event.getButton() instanceof GuiButtonInventoryTab) {
            GuiButtonInventoryTab tab = (GuiButtonInventoryTab) event.getButton();
            Minecraft mc = Minecraft.getMinecraft();

            switch (tab.type) {
                case INVENTORY:
                    mc.displayGuiScreen(new GuiInventory(mc.player));
                    break;
                case SKILLS:
                    mc.displayGuiScreen(new GuiSkills());
                    break;
                case ABILITIES:
                    mc.displayGuiScreen(new GuiAbilities());
                    break;
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void finishRenderTick(RenderTickEvent event) {
        if (event.phase == Phase.END && tooltip != null) {
            RenderHelper.renderTooltip(mx, my, Arrays.asList(new String[]{tooltip}));
            tooltip = null;
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onPotionShiftEvent(GuiScreenEvent.PotionShiftEvent event) {
        if (ConfigHandler.enableTabs) {
            event.setCanceled(true);
        }
    }

    public static int getPotionOffset() {
        return ConfigHandler.enableTabs ? 156 : 124;
    }

}
