package codersafterdark.reskillable.network;

import codersafterdark.reskillable.Reskillable;
import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.requirement.SkillRequirement;
import codersafterdark.reskillable.api.requirement.TraitRequirement;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageDataSync implements IMessage, IMessageHandler<MessageDataSync, IMessage> {
    public NBTTagCompound cmp;

    public MessageDataSync() {
    }

    public MessageDataSync(PlayerData data) {
        cmp = new NBTTagCompound();
        data.saveToNBT(cmp);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        cmp = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, cmp);
    }

    @Override
    public IMessage onMessage(MessageDataSync message, MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(() -> handleMessage(message));
        return null;
    }

    @SideOnly(Side.CLIENT)
    public void handleMessage(MessageDataSync message) {
        PlayerData data = PlayerDataHandler.get(Reskillable.proxy.getClientPlayer());
        data.loadFromNBT(message.cmp);
        data.getRequirementCache().invalidateCache(SkillRequirement.class, TraitRequirement.class);
    }
}