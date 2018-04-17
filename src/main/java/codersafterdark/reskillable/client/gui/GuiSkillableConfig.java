package codersafterdark.reskillable.client.gui;

import codersafterdark.reskillable.base.ConfigHandler;
import codersafterdark.reskillable.lib.LibMisc;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GuiSkillableConfig extends GuiConfig {

    public GuiSkillableConfig(GuiScreen parentScreen) {
        super(parentScreen, getAllElements(), LibMisc.MOD_ID, false, false, GuiConfig.getAbridgedConfigPath(ConfigHandler.config.toString()));
    }


    public static List<IConfigElement> getAllElements() {
        List<IConfigElement> list = new ArrayList();

        Set<String> categories = ConfigHandler.config.getCategoryNames();
        for (String s : categories) {
            if (!s.contains(".")) {
                list.add(new DummyConfigElement.DummyCategoryElement(s, s, new ConfigElement(ConfigHandler.config.getCategory(s)).getChildElements()));
            }
        }

        return list;
    }

}