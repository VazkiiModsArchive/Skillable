package codersafterdark.reskillable.base.configs;

import codersafterdark.reskillable.lib.LibMisc;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static codersafterdark.reskillable.base.configs.ConfigUtilities.loadPropBool;

public class ConfigHandler {
    public static Configuration config;
    public static File configDir;

    public static boolean disableSheepWool = true;
    public static boolean enforceFakePlayers = true;
    public static boolean enableTabs = true;
    public static boolean enableLevelUp = true;
    public static boolean hideRequirements = true;

    public static Map<String, Configuration> cachedConfigs = new HashMap<>();

    public static void init(File file) {
        generateFolder(file);
        config = new Configuration(configDir, LibMisc.MOD_ID);
        config.load();
        loadData();
        config.save();
        cachedConfigs.put(LibMisc.MOD_ID, config);
        MinecraftForge.EVENT_BUS.register(ConfigListener.class);
    }

    public static void loadData() {
        disableSheepWool = loadPropBool(config, "Disable Sheep Dropping Wool on Death", "", disableSheepWool);
        enforceFakePlayers = loadPropBool(config, "Enforce requirements on Fake Players", "", true);
        enableTabs = loadPropBool(config, "Enable Reskillable Tabs", "Set this to false if you don't want to use skills, just the advancement locks", true);
        enableLevelUp = loadPropBool(config, "Enable Level-Up Button", "Set this to false to remove the level-up button if you don't want to use another means to leveling-up skills!", true);
        hideRequirements = loadPropBool(config, "Hide Requirements", "Set this to false to not require holding down the shift key to view requirements!", true);
    }

    public static void generateFolder(File cfgDir) {
        File dir = new File(cfgDir, LibMisc.MOD_ID);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        configDir = dir;
    }
}
