package codersafterdark.reskillable.api.data;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Objects;

public class ItemInfo extends NBTLockKey {
    private int metadata;
    private Item item;

    public ItemInfo(Item item, int metadata) {
        this(item, metadata, null);
    }

    public ItemInfo(Item item, int metadata, NBTTagCompound tag) {
        super(tag);
        this.item = item;
        this.metadata = metadata;
    }

    public ItemInfo(ItemStack stack) {
        this(stack.getItem(), stack.getMetadata(), stack.getTagCompound());
    }

    @Override
    public LockKey getNotFuzzy() {
        return isNotFuzzy() ? this : new ItemInfo(item, metadata);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ItemInfo)) {
            return false;
        }
        ItemInfo other = (ItemInfo) o;
        if (item != other.item) {
            return false;
        }
        if (getTag() == null) {
            return other.getTag() == null && (metadata == OreDictionary.WILDCARD_VALUE || other.metadata == OreDictionary.WILDCARD_VALUE || metadata == other.metadata);
        }
        return getTag().equals(other.getTag()) && (metadata == OreDictionary.WILDCARD_VALUE || other.metadata == OreDictionary.WILDCARD_VALUE || metadata == other.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, tag);
    }
}