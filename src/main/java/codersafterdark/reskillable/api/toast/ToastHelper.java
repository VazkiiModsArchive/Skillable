package codersafterdark.reskillable.api.toast;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ToastHelper {
    //This technically could work for any IToast but as a helper method might as well make sure it is an AbstractToast
    //in case we decide to modify this method to do more things
    @SideOnly(Side.CLIENT)
    public static void sendToast(AbstractToast toast) {
        Minecraft.getMinecraft().getToastGui().add(toast);
    }
}