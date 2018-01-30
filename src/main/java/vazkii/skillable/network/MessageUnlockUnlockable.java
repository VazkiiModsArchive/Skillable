package vazkii.skillable.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import vazkii.arl.network.NetworkMessage;
import vazkii.skillable.base.PlayerData;
import vazkii.skillable.base.PlayerDataHandler;
import vazkii.skillable.base.PlayerSkillInfo;
import vazkii.skillable.skill.Skill;
import vazkii.skillable.skill.Skills;
import vazkii.skillable.skill.base.Unlockable;

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