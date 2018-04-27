package codersafterdark.reskillable.api.data;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public interface NBTLockKey extends LockKey {
    NBTTagCompound getTag();

    NBTLockKey fromItemStack(ItemStack stack);

    LockKey withoutTag();
}