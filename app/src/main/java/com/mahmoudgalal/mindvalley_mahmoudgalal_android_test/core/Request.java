package com.mahmoudgalal.mindvalley_mahmoudgalal_android_test.core;

/**
 * Created by Mahmoud Galal on 30/09/2016.
 */
public interface Request {

    boolean cancel();

    interface CallBack{
        void onFailure(int code, Object extra);
        void onSuccess(int code, Object extra);
    }
}
