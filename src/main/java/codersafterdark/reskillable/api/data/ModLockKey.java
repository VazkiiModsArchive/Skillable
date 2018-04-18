package codersafterdark.reskillable.api.data;

public class ModLockKey implements LockKey {
    private final String modName;

    public ModLockKey(String modName) {
        this.modName = modName.toLowerCase();
    }

    @Override
    public boolean equals(Object o) {
        return o == this || o instanceof ModLockKey && modName.equals(((ModLockKey) o).modName);
    }

    @Override
    public int hashCode() {
        return modName.hashCode();
    }
}