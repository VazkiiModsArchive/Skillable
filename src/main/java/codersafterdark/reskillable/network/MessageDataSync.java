package codersafterdark.reskillable.network;

import codersafterdark.reskillable.Reskillable;
import codersafterdark.reskillable.base.PlayerData;
import codersafterdark.reskillable.base.PlayerDataHandler;
import codersafterdark.reskillable.client.base.ClientTickHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.network.NetworkMessage;

public class MessageDataSync extends NetworkMessage {

    public NBTTagCompound cmp;

    public MessageDataSync() {
    }

    public MessageDataSync(PlayerData data) {
        cmp = new NBTTagCompound();
        data.saveToNBT(cmp);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IMessage handleMessage(MessageContext context) {
        ClientTickHandler.scheduledActions.add(() -> {
            PlayerData data = PlayerDataHandler.get(Reskillable.proxy.getClientPlayer());
            data.loadFromNBT(cmp);
        });

        return null;
    }

}