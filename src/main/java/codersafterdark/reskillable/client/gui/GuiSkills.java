package codersafterdark.reskillable.client.gui;

import codersafterdark.reskillable.api.ReskillableRegistries;
import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.data.PlayerSkillInfo;
import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.client.base.RenderHelper;
import codersafterdark.reskillable.client.gui.handler.InventoryTabHandler;
import codersafterdark.reskillable.lib.LibMisc;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiSkills extends GuiScreen {

    public static final ResourceLocation SKILLS_RES = new ResourceLocation(LibMisc.MOD_ID, "textures/gui/skills.png");

    private int guiWidth, guiHeight;
    private Skill hoveredSkill;

    private int offset = 0;

    private int left;
    private int top;
    private int lastY;

    private List<Skill> skills;

    public GuiSkills() {
        skills = new ArrayList<>();
        ReskillableRegistries.SKILLS.getValuesCollection().stream().filter(Skill::isEnabled).forEach(skills::add);
    }

    public static void drawSkill(int x, int y, Skill skill) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.renderEngine.bindTexture(skill.getSpriteLocation());
        int rank = PlayerDataHandler.get(mc.player).getSkillInfo(skill).getRank();
        Pair<Integer, Integer> pair = skill.getSpriteFromRank(rank);
        RenderHelper.drawTexturedModalRect(x, y, 1, pair.getKey(), pair.getValue(), 16, 16, 1f / 64, 1f / 64);
    }

    public static void drawScrollButtonsTop(int x, int y) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.renderEngine.bindTexture(SKILLS_RES);

        RenderHelper.drawTexturedModalRect(x, y, 1, 0, 230, 80, 4);
    }

    public static void drawScrollButtonsBottom(int x, int y) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.renderEngine.bindTexture(SKILLS_RES);

        RenderHelper.drawTexturedModalRect(x, y, 1, 0, 235, 80, 4);
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

        left = width / 2 - guiWidth / 2;
        top = height / 2 - guiHeight / 2;
        drawTexturedModalRect(left, top, 0, 0, guiWidth, guiHeight);

        PlayerData data = PlayerDataHandler.get(mc.player);

        hoveredSkill = null;


        int index = 0;
        for (int j = offset; j < skills.size() && index < 8; j++) {
            Skill skill = skills.get(j);
            PlayerSkillInfo skillInfo = data.getSkillInfo(skill);

            int i = index++;
            int w = 79;
            int h = 32;
            int x = left + (i % 2) * (w + 3) + 8;
            int y = top + (i / 2) * (h + 3) + 18;

            lastY = y;
            int u = 0;
            int v = guiHeight;

            if (mouseX >= x && mouseY >= y && mouseX < x + w && mouseY < y + h) {
                u += w;
                hoveredSkill = skill;
            }
            if (skillInfo.isCapped()) {
                v += h;
            }

            mc.renderEngine.bindTexture(SKILLS_RES);
            GlStateManager.color(1F, 1F, 1F);
            drawTexturedModalRect(x, y, u, v, w, h);
            drawSkill(x + 5, y + 9, skill);

            mc.fontRenderer.drawString(skill.getName(), x + 26, y + 6, 0xFFFFFF);
            mc.fontRenderer.drawString(skillInfo.getLevel() + "/" + skill.getCap(), x + 26, y + 17, 0x888888);
        }
        GL11.glColor4f(1, 1, 1, 1);
        drawScrollButtonsTop(left + 45, top + 18 - 4); //Precalculate ((79 + 3) + 8) / 2 to 45
        drawScrollButtonsBottom(left + 45, lastY + 32); //Precalculate ((79 + 3) + 8) / 2 to 45


        String skillsStr = I18n.translateToLocal("skillable.misc.skills");
        fontRenderer.drawString(skillsStr, width / 2 - fontRenderer.getStringWidth(skillsStr) / 2, top + 5, 4210752);

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
        if (mouseButton == 0) {
            if (mouseX >= left + 45 && mouseX <= left + 45 + 79) { //Precalculate ((79 + 3) + 8) / 2 to 45
                if (mouseY >= top + 14 && mouseY <= top + 14 + 4) {
                    scrollUp();
                } else if (mouseY >= top + 14 && mouseY <= lastY + 36) {
                    scrollDown();
                }
            }
        }

    }

    private void scrollUp() {
        offset = Math.max(offset - 2, 0);
    }

    private void scrollDown() {
        int off = 2;
        if (skills.size() % 2 == 1) {
            off = 1;
        }
        offset = Math.min(offset + 2, Math.max(skills.size() - 6 - off, 0));
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        if (Mouse.getEventDWheel() > 0) {
            scrollUp();
        } else if (Mouse.getEventDWheel() < 0) {
            scrollDown();
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

}
