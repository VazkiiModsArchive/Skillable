package vazkii.skillable.client.gui.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import vazkii.skillable.client.gui.GuiSkillInfo;

public class GuiButtonLevelUp extends GuiButton {

    int cost;

    public GuiButtonLevelUp(int x, int y) {
        super(0, x, y, 14, 14, "");
        cost = Integer.MAX_VALUE;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float f) {
        enabled = mc.player.experienceLevel >= cost || mc.player.isCreative();

        if (enabled) {
            GlStateManager.color(1F, 1F, 1F);
            mc.renderEngine.bindTexture(GuiSkillInfo.SKILL_INFO_RES);

            int x = this.x;
            int y = this.y;
            int u = 176;
            int v = 0;
            int w = width;
            int h = height;

            if (mouseX > this.x && mouseY > this.y && mouseX < this.x + width && mouseY < this.y + height)
                v += h;

            drawTexturedModalRect(x, y, u, v, w, h);
        }
    }

}
