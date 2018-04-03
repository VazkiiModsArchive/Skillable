package codersafterdark.reskillable.api;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.skill.SkillConfig;
import codersafterdark.reskillable.api.unlockable.UnlockableConfig;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Level;

public interface IModAccess {
    SkillConfig getSkillConfig(ResourceLocation name);

    UnlockableConfig getUnlockableConfig(ResourceLocation name, int cost, String[] defaultRequirements);

    void syncPlayerData(EntityPlayer entityPlayer, PlayerData playerData);

    AdvancementProgress getAdvancementProgress(EntityPlayer entityPlayer, Advancement advancement);

    void log(Level warn, String s);
}
