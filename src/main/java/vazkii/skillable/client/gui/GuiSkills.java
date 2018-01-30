package vazkii.skillable.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import vazkii.arl.util.RenderHelper;
import vazkii.skillable.base.ConfigHandler;
import vazkii.skillable.base.PlayerData;
import vazkii.skillable.base.PlayerDataHandler;
import vazkii.skillable.base.PlayerSkillInfo;
import vazkii.skillable.client.gui.handler.InventoryTabHandler;
import vazkii.skillable.lib.LibMisc;
import vazkii.skillable.skill.Skill;
import vazkii.skillable.skill.Skills;

import java.io.IOException;

public class GuiSkills extends GuiScreen {

    public static final ResourceLocation SKILLS_RES = new ResourceLocation(LibMisc.MOD_ID, "textures/gui/skills.png");

    int guiWidth, guiHeight;
    Skill hoveredSkill;

    public static void drawSkill(int x, int y, Skill skill) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.renderEngine.bindTexture(SKILLS_RES);
        int rank = PlayerDataHandler.get(mc.player).getSkillInfo(skill).getRank();
        RenderHelper.drawTexturedModalRect(x, y, 1, 176 + Math.min(rank, 3) * 16, 44 + skill.getIndex() * 16, 16, 16);
    }

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
        GlStateManager.color(1F, 1F, 1F);

        int left = width / 2 - guiWidth / 2;
        int top = height / 2 - guiHeight / 2;
        drawTexturedModalRect(left, top, 0, 0, guiWidth, guiHeight);

        PlayerData data = PlayerDataHandler.get(mc.player);

        hoveredSkill = null;
        for (String s : Skills.SKILLS.keySet()) {
            Skill skill = Skills.SKILLS.get(s);
            PlayerSkillInfo skillInfo = data.getSkillInfo(skill);

            int i = skill.getIndex();
            int w = 79;
            int h = 32;
            int x = left + (i % 2) * (w + 3) + 8;
            int y = top + (i / 2) * (h + 3) + 18;
            int u = 0;
            int v = guiHeight;

            if (mouseX >= x && mouseY >= y && mouseX < x + w && mouseY < y + h) {
                u += w;
                hoveredSkill = skill;
            }
            if (skillInfo.isCapped())
                v += h;

            mc.renderEngine.bindTexture(SKILLS_RES);
            GlStateManager.color(1F, 1F, 1F);
            drawTexturedModalRect(x, y, u, v, w, h);
            drawSkill(x + 5, y + 9, skill);

            mc.fontRenderer.drawString(skill.getName(), x + 26, y + 6, 0xFFFFFF);
            mc.fontRenderer.drawString(skillInfo.getLevel() + "/" + ConfigHandler.levelCap, x + 26, y + 17, 0x888888);
        }

        String skillsStr = I18n.translateToLocal("skillable.misc.skills");
        fontRenderer.drawString(skillsStr, width / 2 - fontRenderer.getStringWidth(skillsStr) / 2, top + 6, 4210752);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (mouseButton == 0 && hoveredSkill != null) {
            mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            GuiSkillInfo gui = new GuiSkillInfo(hoveredSkill);
            mc.displayGuiScreen(gui);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

}
