package com.mahmoudgalal.mindvalley_mahmoudgalal_android_test.core;

import android.content.Context;
import android.widget.ImageView;

import com.mahmoudgalal.mindvalley_mahmoudgalal_android_test.core.utils.BitmapCache;
import com.mahmoudgalal.mindvalley_mahmoudgalal_android_test.core.utils.ImageCache;
import com.mahmoudgalal.mindvalley_mahmoudgalal_android_test.core.utils.JsonCache;
import com.mahmoudgalal.mindvalley_mahmoudgalal_android_test.core.utils.JsonCacheImpl;


/**
 * Created by Mahmoud galal on 30/09/2016.
 */
public class Fetcher {

    private static Fetcher instance;
    private Context context;
    private final ImageCache imageCache;

    private final JsonCache jsonCache;
    private Fetcher(Context context){
        this.context =  context;
        imageCache = new BitmapCache(context);
        jsonCache = new JsonCacheImpl(4*1024);
    }
    private Fetcher(){
        imageCache = new BitmapCache(4*1024*1024);
        jsonCache = new JsonCacheImpl(4*1024);
    }
    public  static  synchronized Fetcher getInstance(Context context){
        if (instance == null)
            instance = context == null?new Fetcher():new Fetcher(context);
        return instance;
    }
    public ImageCache getImageCache() {
        return imageCache;
    }
    public JsonCache getJsonCache() {
        return jsonCache;
    }

    public void clearCache(){
        ((BitmapCache)imageCache).evictAll();
        ((JsonCacheImpl)jsonCache).evictAll();
    }

    public ImageRequest newImageRequest(String url,String tag,ImageView imageView ,
                                Request.CallBack callBack){
        return  new ImageRequest(url,tag,imageView,callBack);
    }

    public <T>JsonRequest<T> newJsonRequest(String url,String tag,Class<T> tClass,
                                            Request.CallBack callBack){
        return  new JsonRequest<T>(url,tag,tClass,callBack);
    }

}
