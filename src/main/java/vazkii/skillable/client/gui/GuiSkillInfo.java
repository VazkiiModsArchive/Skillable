package vazkii.skillable.client.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.b3d.B3DModel.Texture;
import vazkii.skillable.base.PlayerData;
import vazkii.skillable.base.PlayerDataHandler;
import vazkii.skillable.base.PlayerSkillInfo;
import vazkii.skillable.client.gui.handler.InventoryTabHandler;
import vazkii.skillable.lib.LibMisc;
import vazkii.skillable.skill.Skill;
import vazkii.skillable.skill.Skills;

public class GuiSkillInfo extends GuiScreen {

	public static final ResourceLocation SKILL_INFO_RES = new ResourceLocation(LibMisc.MOD_ID, "textures/gui/skill_info.png");
	
	private final Skill skill;
	
	int guiWidth, guiHeight;
	TextureAtlasSprite sprite;
	
	public GuiSkillInfo(Skill skill) {
		this.skill = skill;
	}
	
	@Override
	public void initGui() {
		guiWidth = 176;
		guiHeight = 166;
		
		buttonList.clear();
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
		
		mc.fontRendererObj.drawString(skill.getName(), left + 42, top + 8, 4210752);
		mc.fontRendererObj.drawString(skillInfo.getLevel() + "/" + PlayerSkillInfo.MAX_LEVEL + "", left + 42, top + 18, 4210752);
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
    private TextureAtlasSprite getTexture(Block blockIn) {
        return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(blockIn.getDefaultState());
    }
	
}
