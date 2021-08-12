package org.deep.caffeine.model;

public class DiffString {
    private final String thing;
    private final DiffStringType type;

    public DiffString(String thing, DiffStringType type) {
        this.thing = thing;
        this.type = type;
    }

    @Override
    public String toString() {
        return "DiffString{" +
                "thing='" + thing + '\'' +
                ", type=" + type +
                '}';
    }
}
