package codersafterdark.reskillable.api.data;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class GenericNBTLockKey implements NBTLockKey {
    private NBTTagCompound tag;

    public GenericNBTLockKey(NBTTagCompound tag) {
        this.tag = tag;
    }

    public GenericNBTLockKey(ItemStack stack) {
        this(stack.getTagCompound());
    }

    @Override
    public NBTTagCompound getTag() {
        return tag;
    }

    @Override
    public LockKey withoutTag() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        return o == this || o instanceof GenericNBTLockKey && (tag == null ? ((GenericNBTLockKey) o).tag == null : tag.equals(((GenericNBTLockKey) o).tag));
    }

    @Override
    public int hashCode() {
        return tag == null ? super.hashCode() : tag.hashCode();
    }
}