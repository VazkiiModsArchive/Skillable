package codersafterdark.reskillable.base.configs;

import codersafterdark.reskillable.lib.LibMisc;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static codersafterdark.reskillable.base.configs.ConfigUtilities.loadPropBool;

public class ConfigHandler {
    public static Configuration mainConfig;
    public static File configDir = new File(LibMisc.MOD_ID);

    public static boolean disableSheepWool = true;
    public static boolean enforceFakePlayers = true;
    public static boolean enableTabs = true;
    public static boolean enableLevelUp = true;
    public static boolean hideRequirements = true;

    public static Map<String, Configuration> cachedConfigs = new HashMap<>();

    public static void init() {
        generateFolder(new File(LibMisc.MOD_ID));
        mainConfig = new Configuration(configDir, LibMisc.MOD_ID);

        mainConfig.load();
        loadData();

        cachedConfigs.put(LibMisc.MOD_ID, mainConfig);

        MinecraftForge.EVENT_BUS.register(ConfigListener.class);
    }

    public static void loadData() {
        disableSheepWool = loadPropBool(mainConfig, "Disable Sheep Dropping Wool on Death", "", disableSheepWool);
        enforceFakePlayers = loadPropBool(mainConfig, "Enforce requirements on Fake Players", "", true);
        enableTabs = loadPropBool(mainConfig, "Enable Reskillable Tabs", "Set this to false if you don't want to use skills, just the advancement locks", true);
        enableLevelUp = loadPropBool(mainConfig, "Enable Level-Up Button", "Set this to false to remove the level-up button if you don't want to use another means to leveling-up skills!", true);
        hideRequirements = loadPropBool(mainConfig, "Hide Requirements", "Set this to false to not require holding down the shift key to view requirements!", true);

        if (mainConfig.hasChanged()) {
            mainConfig.save();
        }
    }

    public static void generateFolder(File cfgDir) {
        File dir = new File(cfgDir, LibMisc.MOD_ID);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        configDir = dir;
    }
}
