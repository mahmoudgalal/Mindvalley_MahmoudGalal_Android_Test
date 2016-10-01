package com.mahmoudgalal.mindvalley_mahmoudgalal_android_test.core.utils;

import android.support.v4.util.LruCache;

/**
 * Created by Mahmoud Galal on 30/09/2016.
 */
public class JsonCacheImpl  extends LruCache<String, String>
        implements JsonCache  {
    /**
     * @param maxSize for caches that do not override {@link #sizeOf}, this is
     *                the maximum number of entries in the cache. For all other caches,
     *                this is the maximum sum of the sizes of the entries in this cache.
     */
    public JsonCacheImpl(int maxSize) {
        super(maxSize);
    }

    @Override
    protected int sizeOf(String key, String value) {
        return /*value.getBytes().length;*/super.sizeOf(key, value);
    }

    @Override
    public String getJson(String url) {
        return get(url);
    }

    @Override
    public void putJson(String url, String json) {
        put(url,json);
    }
}
