package codersafterdark.reskillable.client.gui.handler;

import codersafterdark.reskillable.Reskillable;
import codersafterdark.reskillable.client.gui.GuiSkills;
import codersafterdark.reskillable.lib.LibMisc;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class KeyBindings {
    public static KeyBinding openGUI = new KeyBinding(Reskillable.proxy.getLocalizedString("key.open_gui"), Keyboard.KEY_Y, Reskillable.proxy.getLocalizedString("key.controls." + LibMisc.MOD_ID));

    public static void init() {
        ClientRegistry.registerKeyBinding(openGUI);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        keyTyped(openGUI);
    }

    private void keyTyped(KeyBinding binding) {
        final Minecraft minecraft = FMLClientHandler.instance().getClient();
        if (binding.isPressed()) {
            if (minecraft.currentScreen == null) {
                Minecraft.getMinecraft().displayGuiScreen(new GuiSkills());
            }
        }
    }
}