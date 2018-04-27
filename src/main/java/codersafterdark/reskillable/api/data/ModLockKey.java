package codersafterdark.reskillable.api.data;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class ModLockKey implements LockKey, NBTLockKey {
    private final String modName;
    private NBTTagCompound tag;

    public ModLockKey(String modName) {
        this.modName = modName.toLowerCase();
    }

    public ModLockKey(String modName, NBTTagCompound tag) {
        this(modName);
        this.tag = tag;
    }

    @Override
    public NBTTagCompound getTag() {
        return tag;
    }

    @Override
    public NBTLockKey fromItemStack(ItemStack stack) {
        ResourceLocation registryName = stack.getItem().getRegistryName();
        if (registryName == null) {
            return null;
        }
        return new ModLockKey(registryName.getResourceDomain(), stack.getTagCompound());
    }

    @Override
    public LockKey withoutTag() {
        return tag == null ? this : new ModLockKey(modName);
    }

    @Override
    public boolean equals(Object o) {//TODO make it check if the tags are equal
        return o == this || o instanceof ModLockKey && modName.equals(((ModLockKey) o).modName);
    }

    @Override
    public int hashCode() {
        return modName.hashCode();
    }
}