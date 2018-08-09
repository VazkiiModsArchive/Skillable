package codersafterdark.reskillable.api.toast;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public abstract class AbstractToast implements IToast {
    private final String title;
    private String description;
    private long firstDrawTime;
    private boolean drawn;
    protected int x = 8, y = 8;
    protected long displayTime = 5000L;

    public AbstractToast(String title, String description) {
        this.title = title;
        this.description = description;
    }

    @Nonnull
    @Override
    public Visibility draw(@Nonnull GuiToast guiToast, long l) {
        if (!this.drawn) {
            this.drawn = true;
            this.firstDrawTime = l;
        }
        Minecraft mc = guiToast.getMinecraft();

        mc.getTextureManager().bindTexture(TEXTURE_TOASTS);
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        boolean hasImage = hasImage();
        int xDif = hasImage ? 0 : this.x;
        guiToast.drawTexturedModalRect(0, 0, 0, 32, 160, 32);
        mc.fontRenderer.drawString(getTitle(), 30 - xDif, 7, -11534256);
        mc.fontRenderer.drawString(getDescription(), 30 - xDif, 18, -16777216);
        if (hasImage) {
            renderImage(guiToast);
        }
        return l - this.firstDrawTime >= displayTime ? Visibility.HIDE : Visibility.SHOW;
    }

    protected void bindImage(GuiToast guiToast, ResourceLocation sprite) {
        guiToast.getMinecraft().renderEngine.bindTexture(sprite);
        GlStateManager.color(1.0F, 1.0F, 1.0F);
    }

    protected abstract void renderImage(GuiToast guiToast);

    protected abstract boolean hasImage();

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }
}