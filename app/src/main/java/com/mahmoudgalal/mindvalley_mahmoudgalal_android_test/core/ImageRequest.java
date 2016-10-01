package com.mahmoudgalal.mindvalley_mahmoudgalal_android_test.core;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.mahmoudgalal.mindvalley_mahmoudgalal_android_test.core.utils.ImageCache;
import com.mahmoudgalal.mindvalley_mahmoudgalal_android_test.core.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Mahmoud Galal on 30/09/2016.
 */
public class ImageRequest implements Request {
    private static  final String TAG = ImageRequest.class.getSimpleName();
    private String urlString;
    private String tag;
    private CallBack callBack;
    private int height,width;
    private WeakReference<ImageView> imgReference;
    private WeakReference<DownloadTask> downloadTaskRef;
    private LayoutListener layoutListener;
    private AtomicBoolean isCanceled = new AtomicBoolean(false);

    public boolean isRunning() {
        return isRunning.get();
    }
    void setIsRunning(boolean isrunning){isRunning.set(isrunning);}
    public String getUrlString() {
        return urlString;
    }

    public void setUrlString(String urlString) {
        this.urlString = urlString;
    }

    private AtomicBoolean isRunning = new AtomicBoolean(false);

    public ImageRequest(String url,String tag,ImageView imageView ,CallBack callBack){
        urlString = url;
        this.tag = tag;
        this.callBack = callBack;
        imgReference = new WeakReference<ImageView>(imageView);
        height = imageView.getHeight();
        width = imageView.getWidth();

        Fetcher fetcher = Fetcher.getInstance(null);
        ImageCache imageCache = fetcher.getImageCache();
        Bitmap bitmap = imageCache.getBitmap(url);
        if(bitmap != null){
            Log.i(TAG, "Loading from cache...");
            imageView.setImageBitmap(bitmap);
            callBack.onSuccess(0,null);
        }else {
            if(height > 0 && width >0)
                onViewReady();
            else {
                layoutListener = new LayoutListener(this);
                ViewTreeObserver observer = imageView.getViewTreeObserver();
                observer.addOnPreDrawListener(layoutListener);
            }
        }
    }
    void onViewReady(){
        clear();
        if(!isCanceled.get() && !isRunning.get()){
            ImageView imageView = imgReference.get();
            width = imageView.getWidth();
            height = imageView.getHeight();
            Log.i(TAG, "ImageView Width=" + width + ", height=" + height);
            DownloadTask task = new DownloadTask();
            downloadTaskRef = new WeakReference<DownloadTask>(task);
            isRunning.set(true);
            task.execute(this);
        }
    }
    AsyncTask execute(){
        return  null;
    }

    void clear(){
        if(layoutListener != null){
           ImageView imageView = imgReference.get();
            if(imageView != null){
                imageView.getViewTreeObserver().
                        removeOnPreDrawListener(layoutListener);
            }
        }

    }

    public boolean cancel(){
        if(!isRunning.get())
            return  false;
        isRunning.set(false);
        isCanceled.set(true);
        Log.i(TAG, "Image download canceled..........");
        callBack.onFailure(-2, null);
        if(downloadTaskRef!= null && downloadTaskRef.get()!= null)
            return  downloadTaskRef.get().cancel(true);
        return  false;
    }

    static class LayoutListener implements ViewTreeObserver.OnPreDrawListener {
        private ImageRequest imgRequest;
        LayoutListener(ImageRequest request){
            imgRequest = request;
        }
        @Override
        public boolean onPreDraw() {
            if(imgRequest != null )
                imgRequest.onViewReady();
            return true;
        }
    }

    static class DownloadTask extends AsyncTask<ImageRequest,Void,Bitmap> {
        private ImageRequest imageRequest;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(ImageRequest... imageRequests) {

            ImageRequest request = imageRequests[0];
            imageRequest =request;
            String url = request.getUrlString();
            Log.i(ImageRequest.TAG, "Downloading Image :" + url);
            int reqWidth = request.getWidth();
            int reqHeight = request.getHeight();
            if(!request.isCanceled()) {
                byte[] imageData = downloadImage(url,request.getIsCanceled());
                if (imageData != null) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeByteArray(imageData, 0, imageData.length, options);
                    int imageHeight = options.outHeight;
                    int imageWidth = options.outWidth;
                    String imageType = options.outMimeType;

                    // Calculate inSampleSize
                    options.inSampleSize = Utils.calculateInSampleSize(options,
                            reqWidth, reqHeight);
                    Log.i(TAG, "Image Width=" + imageWidth + ", height=" + imageHeight + " type=" + imageType +
                            " inSampleSize= " + options.inSampleSize);
                    // Decode bitmap with inSampleSize set
                    options.inJustDecodeBounds = false;
                    Log.i(ImageRequest.TAG, "Downloading Image Completed........");
                    return BitmapFactory.decodeByteArray(imageData, 0, imageData.length, options);
                }
            }
            //int width = request.get
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            WeakReference<ImageView> imgView = imageRequest.getImgReference();
            CallBack callBack = imageRequest.getCallBack();
            if(imgView.get()!= null && !imageRequest.isCanceled()) {
                if (bitmap != null) {
                    callBack.onSuccess(0,null);
                    ImageCache cache = Fetcher.getInstance(null).getImageCache();
                    cache.putBitmap(imageRequest.getUrlString(),bitmap);
                    imgView.get().setImageBitmap(bitmap);
                } else {
                    callBack.onFailure(-1,null);
                }
            }else{
                callBack.onFailure(-2,null);
            }
            imageRequest.setIsRunning(false);
        }

        byte[] downloadImage(String urlst,AtomicBoolean canceled)
        {
            InputStream input = null;

            // urlst = new String(urlst.getBytes(), Charset.forName("ISO-8859-15"));
            HttpURLConnection connection = null;
            try {
               // int lastSlash = getLastIndexOf(urlst,'/');
                //String st = urlst.substring(lastSlash+1);//URLEncoder.encode(urlst);
                //encoding no-ascii file names
                //st = URLEncoder.encode(st);
                //urlst = urlst.substring(0, lastSlash+1)+st;
                URL url =  new URL(urlst);

                connection = (HttpURLConnection) url.openConnection();
                //connection.setRequestProperty("Accept-Charset", "UTF-8");
                // connection.setRequestProperty("Content-Type", "image/svg+xml; charset=ISO-8859-15");
                connection.connect();
                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    System.out.println("Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage());
                    return null;
                }
                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = 0;//connection.getContentLength();

                // download the file
                input = connection.getInputStream();

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                ArrayList<Byte> bytes = new ArrayList<>();
                while ((count = input.read(data)) != -1) {
                    if(canceled.get())
                        return null;
                    for(int i = 0;i<count;i++){
                        bytes.add(data[i]);
                    }
                    total += count;
                    // publishing the progress....
                    //if (fileLength > 0) // only if total length is known
                    //publishProgress((int) (total * 100 / fileLength));
                    //output.write(data, 0, count);
                }
                return Utils.toPrimitive(bytes.toArray(new Byte[]{}));
            } catch (Exception e) {
                e.printStackTrace();
                //return null;
                //return e.toString();
            } finally {
                try {

                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                    ignored.printStackTrace();
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }


    }


    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public CallBack getCallBack() {
        return callBack;
    }

    public LayoutListener getLayoutListener() {
        return layoutListener;
    }
    public boolean isCanceled(){
        return isCanceled.get();
    }
    public AtomicBoolean getIsCanceled() {
        return isCanceled;
    }

    public WeakReference<ImageView> getImgReference() {
        return imgReference;
    }

}
