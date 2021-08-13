package org.deep.caffeine.model;

public class DiffString {
    private final String text;
    private final DiffStringType type;

    public DiffString(String text, DiffStringType type) {
        this.text = text;
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public DiffStringType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "DiffString{" +
                "text='" + text + '\'' +
                ", type=" + type +
                '}';
    }
}
