package org.deep.caffeine.cache;

import java.io.*;
import java.util.*;

public class Cache {
    private String directory;
    private final Map<String, CacheEntry> entries;

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public Cache() {
        this.directory = "";
        this.entries = new HashMap<>();
    }

    public void addEntry(File file, CacheEntry entry) {
        this.entries.put(file.getPath(), entry);
    }

    public CacheEntry getEntry(File file) {
        var dir = file.getPath();
        return this.entries.get(dir);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cache cache = (Cache) o;
        return Objects.equals(directory, cache.directory) && Objects.equals(entries, cache.entries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(directory, entries);
    }
}
