package codersafterdark.reskillable.base.configs;

import codersafterdark.reskillable.lib.LibMisc;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ConfigListener {
    @SubscribeEvent
    public void onCfgChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (LibMisc.MOD_ID.equals(event.getModID())) {
            Configuration cfg = ConfigHandler.cachedConfigs.get(event.getModID());
            if (cfg != null) {
                cfg.save();
                ConfigHandler.loadData();
            }
        }
    }
}
