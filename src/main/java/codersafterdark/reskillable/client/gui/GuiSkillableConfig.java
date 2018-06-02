package codersafterdark.reskillable.client.gui;

import codersafterdark.reskillable.base.ConfigHandler;
import codersafterdark.reskillable.lib.LibMisc;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.List;
import java.util.stream.Collectors;

public class GuiSkillableConfig extends GuiConfig {
    public GuiSkillableConfig(GuiScreen parentScreen) {
        super(parentScreen, getAllElements(), LibMisc.MOD_ID, false, false, GuiConfig.getAbridgedConfigPath(ConfigHandler.config.toString()));
    }

    public static List<IConfigElement> getAllElements() {
        return ConfigHandler.config.getCategoryNames().stream().filter(s -> !s.contains(".")).map(s -> new DummyConfigElement.DummyCategoryElement(s, s,
                new ConfigElement(ConfigHandler.config.getCategory(s)).getChildElements())).collect(Collectors.toList());
    }
}