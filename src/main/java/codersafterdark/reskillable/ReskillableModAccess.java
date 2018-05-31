package codersafterdark.reskillable;

import codersafterdark.reskillable.api.IModAccess;
import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.RequirementHolder;
import codersafterdark.reskillable.api.skill.SkillConfig;
import codersafterdark.reskillable.api.unlockable.UnlockableConfig;
import codersafterdark.reskillable.base.ConfigHandler;
import codersafterdark.reskillable.network.MessageDataSync;
import codersafterdark.reskillable.network.PacketHandler;
import com.google.common.collect.Maps;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Level;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class ReskillableModAccess implements IModAccess {
    @Override
    @Nonnull
    public SkillConfig getSkillConfig(ResourceLocation name) {
        SkillConfig skillConfig = new SkillConfig();
        String categoryName = "skill." + name.toString();
        skillConfig.setEnabled(ConfigHandler.config.get(categoryName, "Enabled", skillConfig.isEnabled()).getBoolean());
        skillConfig.setLevelCap(ConfigHandler.config.get(categoryName, "Level Cap", skillConfig.getLevelCap()).getInt());
        skillConfig.setBaseLevelCost(ConfigHandler.config.get(categoryName, "Base Level Cost", skillConfig.getBaseLevelCost()).getInt());
        skillConfig.setSkillPointInterval(ConfigHandler.config.get(categoryName, "Skill Point Interval", skillConfig.getSkillPointInterval()).getInt());
        String[] levelMapping = ConfigHandler.config.get(categoryName, "Level Staggering", new String[]{"1|1"}).getStringList();
        Map<Integer, Integer> configLevelStaggering = Arrays.stream(levelMapping)
                .map(string -> string.split("\\|"))
                .filter(array -> array.length == 2)
                .map(array -> Pair.of(array[0], array[1]))
                .map(pair -> Pair.of(Integer.parseInt(pair.getKey()), Integer.parseInt(pair.getValue())))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));

        Map<Integer, Integer> levelStaggering = Maps.newHashMap();

        int lastLevel = skillConfig.getBaseLevelCost();
        for (int i = 1; i < skillConfig.getLevelCap(); i++) {
            if (configLevelStaggering.containsKey(i)) {
                lastLevel = configLevelStaggering.get(i);
            }
            levelStaggering.put(i, lastLevel);
        }

        skillConfig.setLevelStaggering(levelStaggering);
        return skillConfig;
    }

    @Override
    @Nonnull
    public UnlockableConfig getUnlockableConfig(ResourceLocation name, int x, int y, int cost, String[] defaultRequirements) {
        UnlockableConfig unlockableConfig = new UnlockableConfig();
        String categoryName = "trait." + name.toString();
        unlockableConfig.setEnabled(ConfigHandler.config.get(categoryName, "Enabled", unlockableConfig.isEnabled()).getBoolean());
        unlockableConfig.setX(ConfigHandler.config.get(categoryName, "X-Pos [0-4]:", x).getInt());
        unlockableConfig.setY(ConfigHandler.config.get(categoryName, "Y-Pos [0-3]:", y).getInt());
        unlockableConfig.setCost(ConfigHandler.config.get(categoryName, "Skill Point Cost", cost).getInt());
        unlockableConfig.setRequirementHolder(RequirementHolder.fromStringList(ConfigHandler.config.get(categoryName, "Requirements", defaultRequirements).getStringList()));
        return unlockableConfig;
    }

    @Override
    public void syncPlayerData(EntityPlayer entityPlayer, PlayerData playerData) {
        if (entityPlayer instanceof EntityPlayerMP) {
            MessageDataSync message = new MessageDataSync(playerData);
            PacketHandler.INSTANCE.sendTo(message, (EntityPlayerMP) entityPlayer);
        }
    }

    @Override
    public AdvancementProgress getAdvancementProgress(EntityPlayer entityPlayer, Advancement advancement) {
        return Reskillable.proxy.getPlayerAdvancementProgress(entityPlayer, advancement);
    }

    @Override
    public void log(Level warn, String s) {
        Reskillable.logger.log(warn, s);
    }
}