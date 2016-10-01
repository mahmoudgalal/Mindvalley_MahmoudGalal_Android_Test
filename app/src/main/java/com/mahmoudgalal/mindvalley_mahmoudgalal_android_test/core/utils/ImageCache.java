package com.mahmoudgalal.mindvalley_mahmoudgalal_android_test.core.utils;

import android.graphics.Bitmap;

/**
 * Created by Mahmoud Galal on 30/09/2016.
 */
public interface ImageCache {
    public Bitmap getBitmap(String url);
    public void putBitmap(String url, Bitmap bitmap);
}
