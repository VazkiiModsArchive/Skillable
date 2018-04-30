package codersafterdark.reskillable.api.data;

import net.minecraft.nbt.NBTTagCompound;

public interface NBTLockKey extends LockKey {
    NBTTagCompound getTag();

    LockKey withoutTag();
}