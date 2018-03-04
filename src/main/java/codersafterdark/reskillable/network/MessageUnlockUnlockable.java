package codersafterdark.reskillable.network;

import codersafterdark.reskillable.base.PlayerData;
import codersafterdark.reskillable.base.PlayerDataHandler;
import codersafterdark.reskillable.base.PlayerSkillInfo;
import codersafterdark.reskillable.skill.Skill;
import codersafterdark.reskillable.skill.Skills;
import codersafterdark.reskillable.skill.base.Unlockable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import vazkii.arl.network.NetworkMessage;

public class MessageUnlockUnlockable extends NetworkMessage<MessageUnlockUnlockable> {

    public String skill, unlockable;

    public MessageUnlockUnlockable() {
    }

    public MessageUnlockUnlockable(String skill, String unlockable) {
        this.skill = skill;
        this.unlockable = unlockable;
    }

    @Override
    public IMessage handleMessage(MessageContext context) {
        EntityPlayer player = context.getServerHandler().player;
        Skill skill = Skills.SKILLS.get(this.skill);
        Unlockable unlockable = Skills.ALL_UNLOCKABLES.get(this.unlockable);
        PlayerData data = PlayerDataHandler.get(player);
        PlayerSkillInfo info = data.getSkillInfo(skill);

        if (!info.isUnlocked(unlockable) && info.getSkillPoints() >= unlockable.cost && data.matchStats(unlockable.getRequirements())) {
            info.unlock(unlockable);
            data.saveAndSync();
        }

        return null;
    }
}