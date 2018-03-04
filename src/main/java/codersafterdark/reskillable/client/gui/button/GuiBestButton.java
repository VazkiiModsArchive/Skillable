package codersafterdark.reskillable.client.gui.button;

import net.minecraft.client.gui.GuiButton;

public class GuiBestButton extends GuiButton implements IToolTipProvider {
    public GuiBestButton(int buttonId, int x, int y, String buttonText) {
        super(buttonId, x, y, buttonText);
    }

    @Override
    public ToolTip getToolTip(int mouseX, int mouseY) {
        return null;
    }

    @Override
    public boolean isToolTipVisible() {
        return visible;
    }

    @Override
    public boolean isMouseOver(int mouseX, int mouseY) {
        return isMouseOver(mouseX, mouseY);
    }
}
