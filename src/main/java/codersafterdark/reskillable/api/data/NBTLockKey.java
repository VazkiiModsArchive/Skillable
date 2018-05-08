package codersafterdark.reskillable.api.data;

import net.minecraft.nbt.*;
import net.minecraftforge.common.util.Constants;

import java.util.Set;
import java.util.stream.IntStream;

public abstract class NBTLockKey implements FuzzyLockKey {
    protected NBTTagCompound tag;

    protected NBTLockKey(NBTTagCompound tag) {
        this.tag = tag;
    }

    protected static boolean similarNBT(NBTBase full, NBTBase partial) {
        if (full == null) {
            return partial == null;
        }
        if (partial == null) {
            return true;
        }
        if (full.getId() != partial.getId()) {
            return false;
        }
        if (full.equals(partial)) {
            return true;
        }
        switch (full.getId()) {
            case Constants.NBT.TAG_COMPOUND:
                NBTTagCompound fullTag = (NBTTagCompound) full;
                NBTTagCompound partialTag = (NBTTagCompound) partial;
                Set<String> ptKeys = partialTag.getKeySet();
                for (String partialKey : ptKeys) {
                    //One of the keys is missing OR the tags are different types OR they do not match
                    if (!fullTag.hasKey(partialKey, partialTag.getTagId(partialKey)) || !similarNBT(fullTag.getTag(partialKey), partialTag.getTag(partialKey))) {
                        return false;
                    }
                }
                return true;
            case Constants.NBT.TAG_LIST:
                NBTTagList fTagList = (NBTTagList) full;
                NBTTagList pTagList = (NBTTagList) partial;
                if (fTagList.hasNoTags() && !pTagList.hasNoTags() || fTagList.getTagType() != pTagList.getTagType()) {
                    return false;
                }
                for (int i = 0; i < pTagList.tagCount(); i++) {
                    NBTBase pTag = pTagList.get(i);
                    boolean hasTag = false;
                    for (int j = 0; j < fTagList.tagCount(); j++) {
                        if (similarNBT(fTagList.get(j), pTag)) {
                            hasTag = true;
                            break;
                        }
                    }
                    if (!hasTag) {
                        return false;
                    }
                }
                return true;
            case Constants.NBT.TAG_BYTE_ARRAY:
                byte[] fByteArray = ((NBTTagByteArray) full).getByteArray();
                byte[] pByteArray = ((NBTTagByteArray) partial).getByteArray();
                for (byte pByte : pByteArray) {
                    boolean hasMatch = false;
                    for (byte fByte : fByteArray) {
                        if (pByte == fByte) {
                            hasMatch = true;
                            break;
                        }
                    }
                    if (!hasMatch) {
                        return false;
                    }
                }
                return true;
            case Constants.NBT.TAG_INT_ARRAY:
                int[] fIntArray = ((NBTTagIntArray) full).getIntArray();
                int[] pIntArray = ((NBTTagIntArray) partial).getIntArray();
                for (int pint : pIntArray) {
                    if (IntStream.of(fIntArray).noneMatch(i -> i == pint)) {
                        return false;
                    }
                }
                return true;
            case Constants.NBT.TAG_LONG_ARRAY:
                //Not sure how to get the long array object from this to actually compare them
                return false;
            default:
                return false;
        }
    }

    public NBTTagCompound getTag() {
        return this.tag;
    }

    @Override
    public boolean isNotFuzzy() {
        return this.tag == null;
    }

    @Override
    public boolean fuzzyEquals(FuzzyLockKey other) {
        if (other == this) {
            return true;
        }
        if (other instanceof NBTLockKey) {
            return similarNBT(getTag(), ((NBTLockKey) other).getTag());
        }
        return false;
    }
}