package codersafterdark.reskillable.api.data;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;

public class ModLockKey implements LockKey, NBTLockKey {
    private final String modName;
    private NBTTagCompound tag;

    public ModLockKey(String modName) {
        this.modName = modName == null ? "" : modName.toLowerCase();
    }

    public ModLockKey(String modName, NBTTagCompound tag) {
        this(modName);
        this.tag = tag;
    }

    public ModLockKey(ItemStack stack) {
        ResourceLocation registryName = stack.getItem().getRegistryName();
        if (registryName == null) {
            modName = "";
        } else {
            modName = registryName.getResourceDomain();
            tag = stack.getTagCompound();
        }
    }

    @Override
    public NBTTagCompound getTag() {
        return tag;
    }

    @Override
    public LockKey withoutTag() {
        return tag == null ? this : new ModLockKey(modName);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ModLockKey && modName.equals(((ModLockKey) o).modName)) {
            return tag == null ? ((ModLockKey) o).tag == null : tag.equals(((ModLockKey) o).tag);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(modName, tag);
    }
}