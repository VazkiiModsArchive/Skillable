package codersafterdark.reskillable.api.data;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;

public class ModLockKey extends NBTLockKey {
    private final String modName;

    public ModLockKey(String modName) {
        this(modName, null);
    }

    public ModLockKey(String modName, NBTTagCompound tag) {
        super(tag);
        this.modName = modName == null ? "" : modName.toLowerCase();
    }

    public ModLockKey(ItemStack stack) {
        super(stack.getTagCompound());
        ResourceLocation registryName = stack.getItem().getRegistryName();
        this.modName = registryName == null ? "" : registryName.getResourceDomain();
    }

    @Override
    public LockKey getNotFuzzy() {
        return isNotFuzzy() ? this : new ModLockKey(modName);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ModLockKey && getModName().equals(((ModLockKey) o).getModName())) {
            return getTag() == null ? ((ModLockKey) o).getTag() == null : getTag().equals(((ModLockKey) o).getTag());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(modName, tag);
    }

    public String getModName() {
        return modName;
    }
}
