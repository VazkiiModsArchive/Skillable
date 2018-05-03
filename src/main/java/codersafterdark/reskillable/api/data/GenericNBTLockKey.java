package codersafterdark.reskillable.api.data;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class GenericNBTLockKey extends NBTLockKey {
    public GenericNBTLockKey(NBTTagCompound tag) {
        super(tag);
    }

    public GenericNBTLockKey(ItemStack stack) {
        this(stack.getTagCompound());
    }

    @Override
    public LockKey getNotFuzzy() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        return o == this || o instanceof GenericNBTLockKey && (getTag() == null ? ((GenericNBTLockKey) o).getTag() == null : getTag().equals(((GenericNBTLockKey) o).getTag()));
    }

    @Override
    public int hashCode() {
        return tag == null ? super.hashCode() : tag.hashCode();
    }
}