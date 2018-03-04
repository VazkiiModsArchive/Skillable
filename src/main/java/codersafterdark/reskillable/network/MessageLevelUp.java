package codersafterdark.reskillable.network;

import codersafterdark.reskillable.base.ConfigHandler;
import codersafterdark.reskillable.base.PlayerData;
import codersafterdark.reskillable.base.PlayerDataHandler;
import codersafterdark.reskillable.base.PlayerSkillInfo;
import codersafterdark.reskillable.skill.Skill;
import codersafterdark.reskillable.skill.Skills;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import vazkii.arl.network.NetworkMessage;

public class MessageLevelUp extends NetworkMessage<MessageLevelUp> {

    public String skill;

    public MessageLevelUp() {
    }

    public MessageLevelUp(String skill) {
        this.skill = skill;
    }

    @Override
    public IMessage handleMessage(MessageContext context) {
        EntityPlayer player = context.getServerHandler().player;
        Skill skill = Skills.SKILLS.get(this.skill);
        PlayerData data = PlayerDataHandler.get(player);
        PlayerSkillInfo info = data.getSkillInfo(skill);
        if (!info.isCapped()) {
            int cost = info.getLevelUpCost();
            if (!ConfigHandler.useExperienceNotLevel) {
                if (player.experienceLevel >= cost || player.isCreative()) {
                    if (!player.isCreative()) {
                        player.addExperienceLevel(-cost);
                    }
                    info.levelUp();
                    data.saveAndSync();
                }
            } else if (ConfigHandler.useExperienceNotLevel) {
                if (player.experience >= cost || player.isCreative()) {
                    if (!player.isCreative()) {
                        player.addExperience(-cost);
                    }
                    info.levelUp();
                    data.saveAndSync();
                }
            }
        }
        return null;
    }
}
