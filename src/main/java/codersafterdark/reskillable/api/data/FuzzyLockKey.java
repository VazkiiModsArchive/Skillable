package codersafterdark.reskillable.api.data;

public interface FuzzyLockKey extends LockKey {
    /**
     * Self is the full lock and other is the partial data.
     *
     * @param other The FuzzyLockKey to check.
     * @return True if the two objects count as equals, false otherwise.
     */
    boolean fuzzyEquals(FuzzyLockKey other);

    /**
     * @return True if the fuzzy data is not used in the current instance of the object, false otherwise.
     */
    boolean isNotFuzzy();

    /**
     * @return null if there is no non fuzzy implementation
     */
    LockKey getNotFuzzy();
}