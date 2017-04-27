package vazkii.skillable.client.base;

import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.skillable.base.LevelLockHandler;
import vazkii.skillable.base.PlayerData;
import vazkii.skillable.base.PlayerDataHandler;
import vazkii.skillable.base.RequirementHolder;
import vazkii.skillable.client.gui.GuiSkills;
import vazkii.skillable.skill.Skill;

public class HUDHandler {

	private static ItemStack lockedItem;
	private static String lockMessage;
	private static int lockTime;
	
	public static void setLockMessage(ItemStack item, String message) {
		lockedItem = item;
		lockMessage = message;
		lockTime = 40;
	}
	
	@SubscribeEvent
	public static void renderHUD(RenderGameOverlayEvent event) {
		if(lockTime > 0 && event.getType() == ElementType.ALL) {
			GlStateManager.pushMatrix();
			GlStateManager.disableAlpha();
			GlStateManager.enableBlend();
			float transparency = 1F;
			if(lockTime < 10)
				transparency = 1F / (10 - lockTime + ClientTickHandler.partialTicks);
			
			Minecraft mc = Minecraft.getMinecraft();
			ScaledResolution res = event.getResolution();
			
			int y = res.getScaledHeight() / 2 - 80;
			
			int transparencyInt = (int) (0xFF * transparency) << 24;
			int color = 0xFF3940 + transparencyInt; 
			
			String msg = I18n.translateToLocal(lockMessage);
			int len = mc.fontRendererObj.getStringWidth(msg);
			mc.fontRendererObj.drawStringWithShadow(msg, res.getScaledWidth() / 2 - len / 2, y, color);
			
			PlayerData data = PlayerDataHandler.get(mc.player);
			RequirementHolder reqs = LevelLockHandler.getSkillLock(lockedItem);
			int left = res.getScaledWidth() / 2 - (reqs.getRestrictionLength() * 24) / 2;
			int i = 0;
			for(Skill s : reqs.skillLevels.keySet()) {
				int reqLevel = reqs.skillLevels.get(s);
				int x = left + i * 24;
				GlStateManager.color(1F, 1F, 1F, transparency);
				GuiSkills.drawSkill(x, y + 18, s);
				int level = data.getSkillInfo(s).getLevel();
				color = (level < reqLevel ? 0xFF3940 : 0x39FF8D) + transparencyInt;
				
				mc.fontRendererObj.drawStringWithShadow(Integer.toString(reqLevel), x + 8, y + 32, color);
				i++;
			}
			
			GlStateManager.popMatrix();
		}
	}
	
	public static void tick() {
		if(lockTime > 0)
			lockTime--;
	}
	
	
}
