package codersafterdark.reskillable.network;

import codersafterdark.reskillable.client.base.HUDHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.*;

public class MessageLockedItem implements IMessage, IMessageHandler<MessageLockedItem, IMessage> {
    
    public static final String MSG_ITEM_LOCKED = "skillable.misc.itemLocked";
    public static final String MSG_BLOCK_BREAK_LOCKED = "skillable.misc.blockBreakLocked";
    public static final String MSG_BLOCK_USE_LOCKED = "skillable.misc.blockUseLocked";
    public static final String MSG_ARMOR_EQUIP_LOCKED = "skillable.misc.armorEquipLocked";
    
    public ItemStack stack;
    public String msg;
    
    public MessageLockedItem() {
    }
    
    public MessageLockedItem(ItemStack stack, String msg) {
        this.stack = stack;
        this.msg = msg;
    }
    
    
    @Override
    public void fromBytes(ByteBuf buf) {
        stack = ByteBufUtils.readItemStack(buf);
        msg = ByteBufUtils.readUTF8String(buf);
    }
    
    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeItemStack(buf, stack);
        ByteBufUtils.writeUTF8String(buf, msg);
    }
    
    @Override
    public IMessage onMessage(MessageLockedItem message, MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(() -> {
            handleMessage(message, ctx);
        });
        return null;
    }
    
    public IMessage handleMessage(MessageLockedItem message, MessageContext ctx) {
        HUDHandler.setLockMessage(message.stack, message.msg);
        return null;
    }
}
