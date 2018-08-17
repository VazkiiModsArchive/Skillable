package codersafterdark.reskillable.client.gui;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.data.PlayerSkillInfo;
import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.api.unlockable.Unlockable;
import codersafterdark.reskillable.base.configs.ConfigHandler;
import codersafterdark.reskillable.client.gui.button.GuiButtonLevelUp;
import codersafterdark.reskillable.client.gui.handler.InventoryTabHandler;
import codersafterdark.reskillable.client.gui.handler.KeyBindings;
import codersafterdark.reskillable.lib.LibMisc;
import codersafterdark.reskillable.network.MessageLevelUp;
import codersafterdark.reskillable.network.MessageUnlockUnlockable;
import codersafterdark.reskillable.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static codersafterdark.reskillable.client.base.RenderHelper.renderTooltip;

public class GuiSkillInfo extends GuiScreen {
    public static final ResourceLocation SKILL_INFO_RES = new ResourceLocation(LibMisc.MOD_ID, "textures/gui/skill_info.png");
    public static final ResourceLocation SKILL_INFO_RES2 = new ResourceLocation(LibMisc.MOD_ID, "textures/gui/skill_info2.png");

    private final Skill skill;

    private int guiWidth, guiHeight;
    private ResourceLocation sprite;

    private GuiButtonLevelUp levelUpButton;
    private Unlockable hoveredUnlockable;
    private boolean canPurchase;

    public GuiSkillInfo(Skill skill) {
        this.skill = skill;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == 1) {
            this.mc.displayGuiScreen(null);

            if (this.mc.currentScreen == null) {
                this.mc.setIngameFocus();
            }
        } else if (keyCode == KeyBindings.openGUI.getKeyCode() || keyCode == Minecraft.getMinecraft().gameSettings.keyBindInventory.getKeyCode()) {
            this.mc.displayGuiScreen(null);
            if (this.mc.currentScreen != null) {
                this.mc.setIngameFocus();
            }
        }
    }

    @Override
    public void initGui() {
        guiWidth = 176;
        guiHeight = 166;

        int left = width / 2 - guiWidth / 2;
        int top = height / 2 - guiHeight / 2;

        buttonList.clear();
        if (ConfigHandler.enableLevelUp) {
            buttonList.add(levelUpButton = new GuiButtonLevelUp(left + 147, top + 10));
        }
        InventoryTabHandler.addTabs(this, buttonList);
        sprite = skill.getBackground();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        int left = width / 2 - guiWidth / 2;
        int top = height / 2 - guiHeight / 2;

        PlayerData data = PlayerDataHandler.get(mc.player);
        PlayerSkillInfo skillInfo = data.getSkillInfo(skill);

        mc.renderEngine.bindTexture(sprite);
        GlStateManager.color(0.5F, 0.5F, 0.5F);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 8; j++) {
                drawTexturedRec(left + 16 + i * 16, top + 33 + j * 16, 16, 16);
            }
        }

        GlStateManager.color(1F, 1F, 1F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        if (ConfigHandler.enableLevelUp) {
            mc.renderEngine.bindTexture(SKILL_INFO_RES);
        } else {
            mc.renderEngine.bindTexture(SKILL_INFO_RES2);
        }

        drawTexturedModalRect(left, top, 0, 0, guiWidth, guiHeight);

        GuiSkills.drawSkill(left + 4, top + 9, skill);

        String levelStr = String.format("%d/%d [ %s ]", skillInfo.getLevel(), skill.getCap(), new TextComponentTranslation("skillable.rank." + skillInfo.getRank()).getUnformattedComponentText());
        mc.fontRenderer.drawString(TextFormatting.BOLD + skill.getName(), left + 22, top + 8, 4210752);
        mc.fontRenderer.drawString(levelStr, left + 22, top + 18, 4210752);

        mc.fontRenderer.drawString(new TextComponentTranslation("skillable.misc.skillPoints", skillInfo.getSkillPoints()).getUnformattedComponentText(), left + 15, top + 154, 4210752);

        int cost = skillInfo.getLevelUpCost();
        String costStr = Integer.toString(cost);
        if (skillInfo.isCapped()) {
            costStr = new TextComponentTranslation("skillable.misc.capped").getUnformattedComponentText();
        }

        if (ConfigHandler.enableLevelUp) {
            drawCenteredString(mc.fontRenderer, costStr, left + 138, top + 13, 0xAFFF02);
            levelUpButton.setCost(cost);
        }

        hoveredUnlockable = null;
        skill.getUnlockables().forEach(u -> drawUnlockable(data, skillInfo, u, mouseX, mouseY));
        super.drawScreen(mouseX, mouseY, partialTicks);

        if (hoveredUnlockable != null) {
            makeUnlockableTooltip(data, skillInfo, mouseX, mouseY);
        }
    }

    public void drawTexturedRec(int x, int y, int width, int height) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos((double) x, (double) (y + height), (double) this.zLevel).tex(0, 1).endVertex();
        bufferbuilder.pos((double) (x + width), (double) (y + height), (double) this.zLevel).tex(1, 1).endVertex();
        bufferbuilder.pos((double) (x + width), (double) y, (double) this.zLevel).tex(1, 0).endVertex();
        bufferbuilder.pos((double) x, (double) y, (double) this.zLevel).tex(0, 0).endVertex();
        tessellator.draw();
    }

    private void drawUnlockable(PlayerData data, PlayerSkillInfo info, Unlockable unlockable, int mx, int my) {
        int x = width / 2 - guiWidth / 2 + 20 + unlockable.getX() * 28;
        int y = height / 2 - guiHeight / 2 + 37 + unlockable.getY() * 28;
        mc.renderEngine.bindTexture(SKILL_INFO_RES);
        boolean unlocked = info.isUnlocked(unlockable);

        int u = 0;
        int v = guiHeight;
        if (unlockable.hasSpikes()) {
            u += 26;
        }
        if (unlocked) {
            v += 26;
        }

        GlStateManager.color(1F, 1F, 1F);
        drawTexturedModalRect(x, y, u, v, 26, 26);

        mc.renderEngine.bindTexture(unlockable.getIcon());
        drawModalRectWithCustomSizedTexture(x + 5, y + 5, 0, 0, 16, 16, 16, 16);

        if (mx >= x && my >= y && mx < x + 26 && my < y + 26) {
            canPurchase = !unlocked && info.getSkillPoints() >= unlockable.getCost();
            hoveredUnlockable = unlockable;
        }
    }

    private void makeUnlockableTooltip(PlayerData data, PlayerSkillInfo info, int mouseX, int mouseY) {
        List<String> tooltip = new ArrayList<>();
        TextFormatting tf = hoveredUnlockable.hasSpikes() ? TextFormatting.AQUA : TextFormatting.YELLOW;

        tooltip.add(tf + hoveredUnlockable.getName());

        if (isShiftKeyDown()) {
            addLongStringToTooltip(tooltip, hoveredUnlockable.getDescription(), guiWidth);
        } else {
            tooltip.add(TextFormatting.GRAY + new TextComponentTranslation("skillable.misc.holdShift").getUnformattedComponentText());
            tooltip.add("");
        }

        if (!info.isUnlocked(hoveredUnlockable)) {
            hoveredUnlockable.getRequirements().addRequirementsToTooltip(data, tooltip);
        } else {
            tooltip.add(TextFormatting.GREEN + new TextComponentTranslation("skillable.misc.unlocked").getUnformattedComponentText());
        }

        tooltip.add(TextFormatting.GRAY + new TextComponentTranslation("skillable.misc.skillPoints", hoveredUnlockable.getCost()).getUnformattedComponentText());

        renderTooltip(mouseX, mouseY, tooltip);
    }

    private void addLongStringToTooltip(List<String> tooltip, String longStr, int maxLen) {
        String[] tokens = longStr.split(" ");
        String curr = TextFormatting.GRAY.toString();
        int i = 0;

        while (i < tokens.length) {
            while (fontRenderer.getStringWidth(curr) < maxLen && i < tokens.length) {
                curr = curr + tokens[i] + ' ';
                i++;
            }
            tooltip.add(curr);
            curr = TextFormatting.GRAY.toString();
        }
        tooltip.add(curr);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (ConfigHandler.enableLevelUp && button == levelUpButton) {
            MessageLevelUp message = new MessageLevelUp(skill.getRegistryName());
            PacketHandler.INSTANCE.sendToServer(message);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (mouseButton == 0 && hoveredUnlockable != null && canPurchase) {
            mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            MessageUnlockUnlockable message = new MessageUnlockUnlockable(skill.getRegistryName(), hoveredUnlockable.getRegistryName());
            PacketHandler.INSTANCE.sendToServer(message);
        } else if (mouseButton == 1 || mouseButton == 3) {
            mc.displayGuiScreen(new GuiSkills());
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}