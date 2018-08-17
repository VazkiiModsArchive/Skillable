package codersafterdark.reskillable.base.configs;

import codersafterdark.reskillable.Reskillable;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class ConfigUtilities {
    public static int loadPropInt(Configuration config, String propName, String desc, int default_) {
        Property prop = config.get(Configuration.CATEGORY_GENERAL, propName, default_);
        prop.setComment(desc);
        return prop.getInt(default_);
    }

    public static double loadPropDouble(Configuration config, String propName, String desc, double default_) {
        Property prop = config.get(Configuration.CATEGORY_GENERAL, propName, default_);
        prop.setComment(desc);
        return prop.getDouble(default_);
    }

    public static boolean loadPropBool(Configuration config, String propName, String desc, boolean default_) {
        Property prop = config.get(Configuration.CATEGORY_GENERAL, propName, default_);
        prop.setComment(desc);
        return prop.getBoolean(default_);
    }

    public static void writeStringToFile(String string, File file) {
        boolean exists = file.exists();
        if (!exists) {
            try {
                file.getParentFile().mkdirs();
                exists = file.createNewFile();
            } catch (IOException e) {
                Reskillable.logger.error(e);
            }
        }
        if (exists) {
            try {
                FileUtils.writeStringToFile(file, string, Charset.defaultCharset());
            } catch (IOException e) {
                Reskillable.logger.error(e);
            }
        } else {
            Reskillable.logger.error("Couldn't create File: " + file.getName());
        }
    }
}
