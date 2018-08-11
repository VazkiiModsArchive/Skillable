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

public class SkillToastPacket implements IMessage, IMessageHandler<SkillToastPacket, IMessage> {
    private ResourceLocation skillName;
    private int level;

    public SkillToastPacket() {
    }

    public SkillToastPacket(ResourceLocation skillName, int level) {
        this.skillName = skillName;
        this.level = level;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.skillName = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
        this.level = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.skillName.toString());
        buf.writeInt(this.level);
    }

    @Override
    public IMessage onMessage(SkillToastPacket message, MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(() -> handleMessage(message, ctx));
        return null;
    }

    public IMessage handleMessage(SkillToastPacket message, MessageContext ctx) {
        ToastHelper.sendSkillToast(ReskillableRegistries.SKILLS.getValue(message.skillName), message.level);
        return null;
    }
}