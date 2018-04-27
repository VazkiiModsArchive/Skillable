package codersafterdark.reskillable.api.data;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class GenericNBTLockKey implements NBTLockKey {
    private NBTTagCompound tag;

    public GenericNBTLockKey(NBTTagCompound tag) {
        this.tag = tag;
    }

    //TODO potentially remove, this is just for reverse building
    public GenericNBTLockKey(ItemStack stack) {
        this.tag = stack.getTagCompound();
    }

    @Override
    public NBTTagCompound getTag() {
        return tag;
    }

    @Override
    public NBTLockKey fromItemStack(ItemStack stack) {
        return new GenericNBTLockKey(stack.getTagCompound());
    }

    @Override
    public LockKey withoutTag() {
        return null;
    }
}