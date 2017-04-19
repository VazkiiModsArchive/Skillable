package vazkii.skillable.lib;

import javax.xml.soap.Text;

import net.minecraft.util.text.TextFormatting;

public final class LibMisc {


	public static final String MOD_ID = "skillable";
	public static final String MOD_NAME = MOD_ID;
	public static final String BUILD = "GRADLE:BUILD";
	public static final String VERSION = "GRADLE:VERSION-" + BUILD;
	public static final String DEPENDENCIES = "required-before:autoreglib;";
	
	// Proxy Constants
	public static final String PROXY_COMMON = "vazkii.skillable.base.CommonProxy";
	public static final String PROXY_CLIENT = "vazkii.skillable.client.base.ClientProxy";
	public static final String GUI_FACTORY = "vazkii.skillable.client.gui.GuiFactory";
	
	// Modoff Stuff
	public static final boolean IS_MODOFF = true;
	public static final String[] MODOFF_MESSAGES = new String[] {
		TextFormatting.AQUA + "Skillable " + TextFormatting.RESET + "Modoff Edition!",
		TextFormatting.GREEN + "Need XP? " + TextFormatting.RESET + "Come to the Skillable Booth! We have some demos there too!",
		"WIP Demo. " + TextFormatting.RED + "Do not put this in anywhere you'd actually want to play."
	};
	
}
