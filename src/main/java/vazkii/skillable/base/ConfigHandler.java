package vazkii.skillable.base;

import java.io.File;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.skillable.lib.LibMisc;

public class ConfigHandler {

	public static Configuration config;

	public static int baseXPCost = 4;
	public static int xpIncrease = 1;
	public static int skillPointInterval = 2;
	
	public static void init(File configFile) {
		config = new Configuration(configFile);

		config.load();
		load();

		MinecraftForge.EVENT_BUS.register(ChangeListener.class);
	}

	public static void load() {
		String desc;

		baseXPCost = loadPropInt("Base XP Cost", "", baseXPCost);
		xpIncrease = loadPropInt("XP Increase Per Level", "", 1);
		skillPointInterval = loadPropInt("Levels per Skill Point", "", skillPointInterval);
		
		String[] locks = config.getStringList("Skill Locks", Configuration.CATEGORY_GENERAL, LevelLockHandler.DEFAULT_SKILL_LOCKS, "");
		LevelLockHandler.loadFromConfig(locks);
		
		if(config.hasChanged())
			config.save();
	}

	public static int loadPropInt(String propName, String desc, int default_) {
		Property prop = config.get(Configuration.CATEGORY_GENERAL, propName, default_);
		prop.setComment(desc);

		return prop.getInt(default_);
	}

	public static double loadPropDouble(String propName, String desc, double default_) {
		Property prop = config.get(Configuration.CATEGORY_GENERAL, propName, default_);
		prop.setComment(desc);

		return prop.getDouble(default_);
	}

	public static boolean loadPropBool(String propName, String desc, boolean default_) {
		Property prop = config.get(Configuration.CATEGORY_GENERAL, propName, default_);
		prop.setComment(desc);

		return prop.getBoolean(default_);
	}
	

	public static class ChangeListener {

		@SubscribeEvent
		public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
			if(eventArgs.getModID().equals(LibMisc.MOD_ID))
				load();
		}

	}

	
}
