package codersafterdark.reskillable.network;

import codersafterdark.reskillable.api.ReskillableRegistries;
import codersafterdark.reskillable.api.toast.ToastHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UnlockableToastPacket implements IMessage, IMessageHandler<UnlockableToastPacket, IMessage> {
    private ResourceLocation unlockableName;

    public UnlockableToastPacket() {
    }

    public UnlockableToastPacket(ResourceLocation unlockableName) {
        this.unlockableName = unlockableName;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.unlockableName = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.unlockableName.toString());
    }

    @Override
    public IMessage onMessage(UnlockableToastPacket message, MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(() -> handleMessage(message, ctx));
        return null;
    }

    public IMessage handleMessage(UnlockableToastPacket message, MessageContext ctx) {
        ToastHelper.sendUnlockableToast(ReskillableRegistries.UNLOCKABLES.getValue(message.unlockableName));
        return null;
    }
}