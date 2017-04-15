package vazkii.skillable.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import vazkii.skillable.client.gui.handler.InventoryTabHandler;
import vazkii.skillable.lib.LibMisc;
import vazkii.skillable.skill.Skill;
import vazkii.skillable.skill.Skills;

public class GuiSkills extends GuiScreen {

	public static final ResourceLocation SKILLS_RES = new ResourceLocation(LibMisc.MOD_ID, "textures/gui/skills.png");
	
	int guiWidth, guiHeight;
	
	@Override
	public void initGui() {
		guiWidth = 176;
		guiHeight = 166;
		
		buttonList.clear();
		InventoryTabHandler.addTabs(this, buttonList);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		mc.renderEngine.bindTexture(SKILLS_RES);
		
		int left = width / 2 - guiWidth / 2;
		int top = height / 2 - guiHeight / 2;
		drawTexturedModalRect(left, top, 0, 0, guiWidth, guiHeight);
		
		for(String s : Skills.ALL_SKILLS.keySet()) {
			Skill skill = Skills.ALL_SKILLS.get(s);
			int i = skill.getIndex();
			int w = 79;
			int h = 32;
			int x = left + (i % 2) * (w + 3) + 8;
			int y = top + (i / 2) * (h + 3) + 8;
			int u = 0;
			int v = guiHeight;
			
			if(mouseX >= x && mouseY >= y && mouseX < x + w && mouseY < y + h)
				u += w;
			// TODO move if capped
			
			mc.renderEngine.bindTexture(SKILLS_RES);
			drawTexturedModalRect(x, y, u, v, w, h);
			
			mc.fontRendererObj.drawString(skill.getName(), x + 26, y + 6, 0xFFFFFF);
		}
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
}
