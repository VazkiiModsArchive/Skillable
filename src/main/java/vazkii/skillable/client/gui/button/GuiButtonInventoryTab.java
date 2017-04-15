package vazkii.skillable.client.gui.button;

import java.util.function.Predicate;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.text.translation.I18n;
import vazkii.skillable.base.PlayerData;
import vazkii.skillable.base.PlayerDataHandler;
import vazkii.skillable.client.gui.GuiSkills;
import vazkii.skillable.client.gui.handler.InventoryTabHandler;

public class GuiButtonInventoryTab extends GuiButton {

	public final TabType type;
	private final Predicate<GuiScreen> selectedPred;
	
	public GuiButtonInventoryTab(int id, int x, int y, TabType type, Predicate<GuiScreen> selectedPred) {
		super(id, x, y, 32, 28, "");
		this.type = type;
		this.selectedPred = selectedPred;
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		enabled = type.shouldRender();
		
		GuiScreen curr = mc.currentScreen;
		if(curr instanceof GuiContainerCreative && ((GuiContainerCreative) curr).getSelectedTabIndex() != CreativeTabs.INVENTORY.getTabIndex())
			enabled = false;
		
		if(enabled) {
			GlStateManager.color(1F, 1F, 1F);
			mc.renderEngine.bindTexture(GuiSkills.SKILLS_RES);
			
			int x = xPosition;
			int y = yPosition;
			int u = 176;
			int v = 0;
			int w = width;
			int h = height;
			
			if(isSelected()) {
				x += 4;
				u += w;
			}
			
			drawTexturedModalRect(x, y, u, v, w, h);
			drawTexturedModalRect(x + 8, y + 6, 176 + type.iconIndex * 16, 28, 16, 16);
			
			if(mouseX > xPosition && mouseY > yPosition && mouseX < xPosition + width && mouseY < yPosition + height) {
				InventoryTabHandler.tooltip = I18n.translateToLocal("skillable.tab." + type.name().toLowerCase());
				InventoryTabHandler.mx = mouseX;
				InventoryTabHandler.my = mouseY;
			}
		}
	}
	
	public boolean isSelected() {
		return selectedPred.test(Minecraft.getMinecraft().currentScreen);
	}
	
	public static enum TabType {
		INVENTORY(1, null), 
		SKILLS(0, null), 
		ABILITIES(2, (data) -> data.hasAnyAbilities());
		
		private TabType(int iconIndex, Predicate<PlayerData> render) {
			this.iconIndex = iconIndex;
			this.renderPred = render;
			if(renderPred == null)
				renderPred = (data) -> true;
		}
		
		public final int iconIndex;
		private Predicate<PlayerData> renderPred;
		
		public boolean shouldRender() {
			return renderPred.test(PlayerDataHandler.get(Minecraft.getMinecraft().player));
		}
		
	}
	
}
