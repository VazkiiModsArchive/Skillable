package vazkii.skillable.client.gui.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.text.translation.I18n;
import vazkii.skillable.client.gui.GuiSkillInfo;
import vazkii.skillable.client.gui.GuiSkills;
import vazkii.skillable.client.gui.handler.InventoryTabHandler;

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
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		enabled = mc.player.experienceLevel >= cost || mc.player.isCreative();
		
		if(enabled) {
			GlStateManager.color(1F, 1F, 1F);
			mc.renderEngine.bindTexture(GuiSkillInfo.SKILL_INFO_RES);
			
			int x = xPosition;
			int y = yPosition;
			int u = 176;
			int v = 0;
			int w = width;
			int h = height;
			
			if(mouseX > xPosition && mouseY > yPosition && mouseX < xPosition + width && mouseY < yPosition + height)
				v += h;
			
			drawTexturedModalRect(x, y, u, v, w, h);
		}
	}

}
