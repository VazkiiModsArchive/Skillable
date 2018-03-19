package codersafterdark.reskillable;

import codersafterdark.reskillable.api.IModAccess;
import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.skill.SkillConfig;
import codersafterdark.reskillable.api.unlockable.UnlockableConfig;
import codersafterdark.reskillable.base.ConfigHandler;
import codersafterdark.reskillable.api.data.RequirementHolder;
import codersafterdark.reskillable.network.MessageDataSync;
import codersafterdark.reskillable.network.PacketHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class ReskillableModAccess implements IModAccess {
    @Override
    @Nonnull
    public SkillConfig getSkillConfig(ResourceLocation name) {
        SkillConfig skillConfig = new SkillConfig();
        String categoryName = "skill." + name.toString();
        skillConfig.setEnabled(ConfigHandler.config.get(categoryName, "Enabled", skillConfig.isEnabled()).getBoolean());
        skillConfig.setLevelCap(ConfigHandler.config.get(categoryName, "Level Cap", skillConfig.getLevelCap()).getInt());
        skillConfig.setBaseXPCost(ConfigHandler.config.get(categoryName, "Base XP Cost", skillConfig.getBaseXPCost()).getInt());
        skillConfig.setSkillPointInterval(ConfigHandler.config.get(categoryName, "Skill Point Interval", skillConfig.getSkillPointInterval()).getInt());
        skillConfig.setXpIncrease(ConfigHandler.config.get(categoryName, "XP Increase", skillConfig.getXpIncrease()).getInt());
        skillConfig.setXpIncreaseStagger(ConfigHandler.config.get(categoryName, "XP Level Stagger", skillConfig.getXpIncreaseStagger()).getInt());

        return skillConfig;
    }

    @Override
    @Nonnull
    public UnlockableConfig getUnlockableConfig(ResourceLocation name, int cost, String[] defaultRequirements) {
        UnlockableConfig unlockableConfig = new UnlockableConfig();
        String categoryName = "trait." + name.toString();
        unlockableConfig.setEnabled(ConfigHandler.config.get(categoryName, "Enabled", unlockableConfig.isEnabled()).getBoolean());
        unlockableConfig.setCost(ConfigHandler.config.get(categoryName, "Skill Point Cost", cost).getInt());
        unlockableConfig.setRequirementHolder(RequirementHolder.fromStringList(ConfigHandler.config.get(categoryName, "Requirements", defaultRequirements).getStringList()));
        return unlockableConfig;
    }

    @Override
    public void syncPlayerData(EntityPlayer entityPlayer, PlayerData playerData) {
        if (entityPlayer != null && entityPlayer instanceof EntityPlayerMP) {
            MessageDataSync message = new MessageDataSync(playerData);
            PacketHandler.INSTANCE.sendTo(message, (EntityPlayerMP) entityPlayer);
        }
    }
}
