package vazkii.skillable.network;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.network.NetworkMessage;
import vazkii.skillable.Skillable;
import vazkii.skillable.base.PlayerDataHandler;
import vazkii.skillable.base.PlayerDataHandler.PlayerData;
import vazkii.skillable.client.base.ClientTickHandler;

public class MessageDataSync extends NetworkMessage {

	public NBTTagCompound cmp;

	public MessageDataSync() { }

	public MessageDataSync(PlayerData data) {
		cmp = new NBTTagCompound();
		data.saveToNBT(cmp);
	}	

	@Override
	@SideOnly(Side.CLIENT)
	public IMessage handleMessage(MessageContext context) {
		ClientTickHandler.scheduledActions.add(() -> {
			PlayerData data = PlayerDataHandler.get(Skillable.proxy.getClientPlayer());
			data.loadFromNBT(cmp);
		});

		return null;
	}

}