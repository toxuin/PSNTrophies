package ru.toxuin.psn_trophies.library;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class MemCache {
    private LruCache<String, Bitmap> memoryCache;
    private static MemCache self;

    public static MemCache getInstance() {
        if (self == null) {
            self = new MemCache();
        }
        return self;
    }

    private MemCache() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        memoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            memoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return memoryCache.get(key);
    }
}
