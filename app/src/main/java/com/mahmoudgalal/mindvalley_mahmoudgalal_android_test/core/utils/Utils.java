package com.mahmoudgalal.mindvalley_mahmoudgalal_android_test.core.utils;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;

/**
 * Created by Mahmoud Galal on 30/09/2016.
 */
public class Utils {

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
    public static byte[] toPrimitive(Byte[] bytes){
        byte[] pBytes = new byte[bytes.length];
        for (int i=0;i<bytes.length;i++){
            pBytes[i] = bytes[i];
        }
        return pBytes;
    }
    public static boolean isOnMainThread() {
        return  Looper.myLooper() == Looper.getMainLooper();
    }
    /**
     * Returns whether there is a valid internet connection or no connection
     *
     * @param context
     * @return true if there is internet connection false otherwise
     */
    public static boolean isInternetConnectionExist(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

}
