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

public class MessageLevelUp extends NetworkMessage<MessageLevelUp> {

	public String skill;
	
	public MessageLevelUp() { }
	
	public MessageLevelUp(String skill) {
		this.skill = skill;
	}
	
	@Override
	public IMessage handleMessage(MessageContext context) {
		EntityPlayer player = context.getServerHandler().player;
		Skill skill = Skills.ALL_SKILLS.get(this.skill);
		PlayerData data = PlayerDataHandler.get(player);
		PlayerSkillInfo info = data.getSkillInfo(skill);
		
		if(!info.isCapped()) {
			int cost = info.getLevelUpCost();
			if(player.experienceLevel >= cost || player.isCreative()) {
				if(!player.isCreative())
					player.addExperienceLevel(-cost);
				info.levelUp();
				data.saveAndSync();
			}
		}
		
		return null;
	}
	
}
