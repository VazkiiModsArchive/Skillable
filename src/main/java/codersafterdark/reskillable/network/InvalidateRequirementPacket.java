package codersafterdark.reskillable.network;

import codersafterdark.reskillable.api.requirement.Requirement;
import codersafterdark.reskillable.api.requirement.RequirementCache;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class InvalidateRequirementPacket implements IMessage, IMessageHandler<InvalidateRequirementPacket, IMessage> {
    private Class<? extends Requirement>[] cacheTypes;
    private UUID uuid;

    public InvalidateRequirementPacket() {

    }

    public InvalidateRequirementPacket(UUID uuid, Class<? extends Requirement>... cacheTypes) {
        this.uuid = uuid;
        this.cacheTypes = cacheTypes;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        uuid = new UUID(buf.readLong(), buf.readLong());
        cacheTypes = new Class[buf.readInt()];
        for (int i = 0; i < cacheTypes.length; i++) {
            try {
                Class<?> rClass = Class.forName(ByteBufUtils.readUTF8String(buf));
                if (Requirement.class.isAssignableFrom(rClass)) {
                    cacheTypes[i] = (Class<? extends Requirement>) rClass;
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(uuid.getMostSignificantBits());
        buf.writeLong(uuid.getLeastSignificantBits());
        buf.writeInt(cacheTypes.length);
        for (Class<? extends Requirement> rClass : cacheTypes) {
            ByteBufUtils.writeUTF8String(buf, rClass.getName());
        }
    }

    @Override
    public IMessage onMessage(InvalidateRequirementPacket message, MessageContext ctx) {
        if (ctx.side.isServer()) {
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> handleMessage(message, ctx));
        } else {
            Minecraft.getMinecraft().addScheduledTask(() -> handleMessage(message, ctx));
        }
        return null;
    }

    public IMessage handleMessage(InvalidateRequirementPacket message, MessageContext ctx) {
        RequirementCache.invalidateCacheNoPacket(message.uuid, ctx.side.isClient(), message.cacheTypes);
        return null;
    }
}