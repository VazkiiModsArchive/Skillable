package vazkii.skillable.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import vazkii.arl.network.NetworkHandler;
import vazkii.arl.util.RenderHelper;
import vazkii.skillable.base.PlayerData;
import vazkii.skillable.base.PlayerDataHandler;
import vazkii.skillable.base.PlayerSkillInfo;
import vazkii.skillable.client.gui.button.GuiButtonLevelUp;
import vazkii.skillable.client.gui.handler.InventoryTabHandler;
import vazkii.skillable.lib.LibMisc;
import vazkii.skillable.network.MessageLevelUp;
import vazkii.skillable.skill.Skill;
import vazkii.skillable.skill.base.Unlockable;

public class GuiSkillInfo extends GuiScreen {

	public static final ResourceLocation SKILL_INFO_RES = new ResourceLocation(LibMisc.MOD_ID, "textures/gui/skill_info.png");
	
	private final Skill skill;
	
	int guiWidth, guiHeight;
	TextureAtlasSprite sprite;
	
	GuiButtonLevelUp levelUpButton;
	Unlockable hoveredUnlockable;
	
	public GuiSkillInfo(Skill skill) {
		this.skill = skill;
	}
	
	@Override
	public void initGui() {
		guiWidth = 176;
		guiHeight = 166;
		
		int left = width / 2 - guiWidth / 2;
		int top = height / 2 - guiHeight / 2;
		
		buttonList.clear();
		buttonList.add(levelUpButton = new GuiButtonLevelUp(left + 147, top + 10));
		InventoryTabHandler.addTabs(this, buttonList);
		sprite = getTexture(skill.getBackground());
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		
		int left = width / 2 - guiWidth / 2;
		int top = height / 2 - guiHeight / 2;
		
		PlayerData data = PlayerDataHandler.get(mc.player);
		PlayerSkillInfo skillInfo = data.getSkillInfo(skill);
		
		mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.color(0.5F, 0.5F, 0.5F);
		for(int i = 0; i < 9; i++)
			for(int j = 0; j < 8; j++) {
				int x = left + 16 + i * 16;
				int y = top + 33 + j * 16;
				drawTexturedModalRect(x, y, sprite, 16, 16);
			}
		
		GlStateManager.color(1F, 1F, 1F);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		mc.renderEngine.bindTexture(SKILL_INFO_RES);
		
		drawTexturedModalRect(left, top, 0, 0, guiWidth, guiHeight);
		
		mc.renderEngine.bindTexture(GuiSkills.SKILLS_RES);
		drawTexturedModalRect(left + 15, top + 9, width, 45 + skill.getIndex() * 16, 16, 16);
		
		String levelStr = String.format("%d/%d (%s)", skillInfo.getLevel(), PlayerSkillInfo.MAX_LEVEL , I18n.translateToLocal("skillable.rank." + skillInfo.getRank()));
		mc.fontRendererObj.drawString(TextFormatting.BOLD + skill.getName(), left + 37, top + 8, 4210752);
		mc.fontRendererObj.drawString(levelStr, left + 37, top + 18, 4210752);

		int cost = skillInfo.getLevelUpCost();
		String costStr = Integer.toString(cost);
		if(skillInfo.isCapped())
			costStr = I18n.translateToLocal("skillable.misc.capped");
		
		levelUpButton.setCost(cost);
		
		drawCenteredString(mc.fontRendererObj, costStr, left + 138, top + 13, 0xAFFF02);
		
		hoveredUnlockable = null;
		for(Unlockable u : skill.unlockables)
			drawUnlockable(data, u, mouseX, mouseY);
		
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		if(hoveredUnlockable != null) {
			List<String> tooltip = new ArrayList();
			TextFormatting tf = hoveredUnlockable.hasSpikes() ? TextFormatting.AQUA : TextFormatting.YELLOW;
			tooltip.add(tf + hoveredUnlockable.getName());
			RenderHelper.renderTooltip(mouseX, mouseY, tooltip);
		}
	}
	
	private void drawUnlockable(PlayerData data, Unlockable unlockable, int mx, int my) {
		int x = width / 2 - guiWidth / 2 + 20 + unlockable.x * 28;
		int y = height / 2 - guiHeight / 2 + 37 + unlockable.y * 28;
		mc.renderEngine.bindTexture(SKILL_INFO_RES);
		
		int u = 0;
		int v = guiHeight;
		if(unlockable.hasSpikes())
			u += 26;
	
		GlStateManager.color(1F, 1F, 1F);
		drawTexturedModalRect(x, y, u, v, 26, 26);
		
		mc.renderEngine.bindTexture(unlockable.getIcon());
		drawModalRectWithCustomSizedTexture(x + 5, y + 5, 0, 0, 16, 16, 16, 16);
		
		if(mx >= x && my >= y && mx < x + 26 && my < y + 26)
			hoveredUnlockable = unlockable;
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if(button == levelUpButton) {
			MessageLevelUp message = new MessageLevelUp(skill.getKey());
			NetworkHandler.INSTANCE.sendToServer(message);
		}
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
    private TextureAtlasSprite getTexture(Block blockIn) {
        return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(blockIn.getDefaultState());
    }
	
}
