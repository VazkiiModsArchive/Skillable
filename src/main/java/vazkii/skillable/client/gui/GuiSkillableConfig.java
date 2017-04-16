package vazkii.skillable.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import vazkii.skillable.base.ConfigHandler;
import vazkii.skillable.lib.LibMisc;

public class GuiSkillableConfig extends GuiConfig {

	public GuiSkillableConfig(GuiScreen parentScreen) {
		super(parentScreen, new ConfigElement(ConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(), LibMisc.MOD_ID, false, false, GuiConfig.getAbridgedConfigPath(ConfigHandler.config.toString()));
	}

}