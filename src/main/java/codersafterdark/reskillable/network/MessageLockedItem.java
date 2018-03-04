package codersafterdark.reskillable.network;

import codersafterdark.reskillable.client.base.HUDHandler;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.network.NetworkMessage;

public class MessageLockedItem extends NetworkMessage {

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
    @SideOnly(Side.CLIENT)
    public IMessage handleMessage(MessageContext context) {
        HUDHandler.setLockMessage(stack, msg);
        return null;
    }

}
