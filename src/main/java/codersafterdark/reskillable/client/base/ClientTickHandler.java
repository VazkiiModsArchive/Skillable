package codersafterdark.reskillable.client.base;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;

import java.util.ArrayDeque;
import java.util.Queue;

public class ClientTickHandler {

    public static volatile Queue<Runnable> scheduledActions = new ArrayDeque();

    public static int ticksInGame = 0;
    public static float partialTicks = 0;
    public static float delta = 0;
    public static float total = 0;

    private static void calcDelta() {
        float oldTotal = total;
        total = ticksInGame + partialTicks;
        delta = total - oldTotal;
    }

    @SubscribeEvent
    public static void renderTick(RenderTickEvent event) {
        if (event.phase == Phase.START) {
            partialTicks = event.renderTickTime;
        }
    }

    @SubscribeEvent
    public static void clientTickEnd(ClientTickEvent event) {
        if (event.phase == Phase.END) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.world == null) {
                PlayerDataHandler.cleanup();
            } else if (mc.player != null) {
                while (!scheduledActions.isEmpty()) {
                    scheduledActions.poll().run();
                }
            }

            GuiScreen gui = mc.currentScreen;
            if (gui == null || !gui.doesGuiPauseGame()) {
                ticksInGame++;
                partialTicks = 0;
                HUDHandler.tick();
            }

            calcDelta();
        }
    }

}
