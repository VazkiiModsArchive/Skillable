package codersafterdark.reskillable.client.base;

import codersafterdark.reskillable.base.LevelLockHandler;
import codersafterdark.reskillable.base.PlayerData;
import codersafterdark.reskillable.base.PlayerDataHandler;
import codersafterdark.reskillable.base.RequirementHolder;
import codersafterdark.reskillable.client.gui.GuiSkills;
import codersafterdark.reskillable.network.MessageLockedItem;
import codersafterdark.reskillable.skill.Skill;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.arl.util.RenderHelper;

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
        if (lockTime > 0 && event.getType() == ElementType.ALL) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableAlpha();
            float transparency = 1F;
            if (lockTime < 10)
                transparency = Math.max(0.05F, (lockTime - ClientTickHandler.partialTicks) / 10F);

            Minecraft mc = Minecraft.getMinecraft();
            ScaledResolution res = event.getResolution();

            int width = res.getScaledWidth();
            int height = res.getScaledHeight();
            int y = height / 2 - 80;
            if (lockMessage.equals(MessageLockedItem.MSG_ARMOR_EQUIP_LOCKED))
                y -= 30;

            int transparencyInt = (int) (0xFF * transparency) << 24;
            int color = (int) (0x11 * transparency) << 24;

            String msg = I18n.translateToLocal(lockMessage);
            int len = mc.fontRenderer.getStringWidth(msg);

            PlayerData data = PlayerDataHandler.get(mc.player);
            RequirementHolder reqs = LevelLockHandler.getSkillLock(lockedItem);
            int pad = 26;
            int left = width / 2 - (reqs.getRestrictionLength() * pad) / 2;
            int xp = left;

            int boxWidth = Math.max(pad * reqs.getRestrictionLength(), len) + 20;
            int boxHeight = 52 + reqs.advancements.size() * 10;
            Gui.drawRect(width / 2 - boxWidth / 2, y - 10, width / 2 + boxWidth / 2, y + boxHeight, color);
            Gui.drawRect(width / 2 - boxWidth / 2 - 2, y - 12, width / 2 + boxWidth / 2 + 2, y + boxHeight + 2, color);

            GlStateManager.enableBlend();
            color = 0xFF3940 + transparencyInt;
            mc.fontRenderer.drawStringWithShadow(msg, res.getScaledWidth() / 2 - len / 2, y, color);

            for (Skill s : reqs.skillLevels.keySet()) {
                int reqLevel = reqs.skillLevels.get(s);
                GlStateManager.color(1F, 1F, 1F, transparency);
                GuiSkills.drawSkill(xp, y + 18, s);
                int level = data.getSkillInfo(s).getLevel();
                color = (level < reqLevel ? 0xFF3940 : 0x39FF8D) + transparencyInt;

                mc.fontRenderer.drawStringWithShadow(Integer.toString(reqLevel), xp + 8, y + 32, color);
                xp += pad;
            }

            int textLeft = xp;
            int textY = y + 48;
            color = 0xFFFFFF + transparencyInt;

            for (ResourceLocation advRes : reqs.advancements) {
                Advancement adv = reqs.getAdvancementList().getAdvancement(advRes);
                if (adv == null)
                    return;

                mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/advancements/widgets.png"));
                DisplayInfo display = adv.getDisplay();
                int u = display.getFrame().getIcon();
                int v = 154;

                GlStateManager.color(1F, 1F, 1F, transparency);
                RenderHelper.drawTexturedModalRect(xp - 3, y + 17, 0, u, v, 26, 26);
                GlStateManager.disableLighting();
                GlStateManager.enableCull();
                net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
                GlStateManager.pushMatrix();
                GlStateManager.translate(xp + 2, y + 22, 0);
                GlStateManager.scale(1, transparency, 1);
                mc.getRenderItem().renderItemAndEffectIntoGUI(display.getIcon(), 0, 0);
                GlStateManager.popMatrix();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                GlStateManager.disableLighting();

                mc.fontRenderer.drawStringWithShadow(adv.getDisplayText().getUnformattedText(), textLeft, textY, color);

                xp += pad;
                textY += 11;
            }

            GlStateManager.popMatrix();
        }
    }

    public static void tick() {
        if (lockTime > 0)
            lockTime--;
    }


}
