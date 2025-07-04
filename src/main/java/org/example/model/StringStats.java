package org.example.model;

public class StringStats {
    private final int minLength;
    private final int maxLength;

    public StringStats(int minLength, int maxLength) {
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    public int getMinLength() {
        return minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }
}
