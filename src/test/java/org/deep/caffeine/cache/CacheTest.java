package org.deep.caffeine.cache;

import com.google.gson.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;

public class CacheTest {
    @Test
    public void testJsonDeserialize() {
        var cache = new Cache();
        cache.setDirectory("/home/dknite/work/comp");
        CacheEntry entry;
        entry = new CacheEntry("piglatin\ninput", "iglatinpay\ninputay");
        cache.addEntry(new File("/home/dknite/work/comp/cf3d665/A.cpp"), entry);
        entry = new CacheEntry("1 2 3", "3 2 1");
        cache.addEntry(new File("/home/dknite/work/comp/cf3d665/B.cpp"), entry);

        var gson = new Gson();
        var json = gson.toJson(cache);
        var newCache = gson.fromJson(json, Cache.class);
        assertEquals(cache, newCache);
    }
}
