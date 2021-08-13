package org.deep.caffeine.cache;

import java.util.*;

public class CacheEntry {
    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getExpected() {
        return expected;
    }

    public void setExpected(String expected) {
        this.expected = expected;
    }

    private String input;
    private String expected;

    public CacheEntry(String input, String expected) {
        this.input = input;
        this.expected = expected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CacheEntry entry = (CacheEntry) o;
        return Objects.equals(input, entry.input) && Objects.equals(expected, entry.expected);
    }

    @Override
    public int hashCode() {
        return Objects.hash(input, expected);
    }
}
