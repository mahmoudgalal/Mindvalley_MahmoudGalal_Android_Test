package com.mahmoudgalal.mindvalley_mahmoudgalal_android_test.core;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.mahmoudgalal.mindvalley_mahmoudgalal_android_test.core.utils.JsonCache;
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
public class JsonRequest<T> implements Request {
    private static  final String TAG = JsonRequest.class.getSimpleName();
    private String urlString;
    private String tag;
    private CallBack callBack;

    public CallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUrlString() {
        return urlString;
    }

    public void setUrlString(String urlString) {
        this.urlString = urlString;
    }

    public Class<T> getClassObject() {
        return classObject;
    }

    public void setClassObject(Class<T> classObject) {
        this.classObject = classObject;
    }

    public boolean isCanceled() {
        return isCanceled.get();
    }
    AtomicBoolean getIsCanceled() {
        return isCanceled ;
    }

    void setIsCanceled(boolean iscanceled) {
        this.isCanceled.set(iscanceled);
    }

    public boolean isRunning() {
        return isRunning.get();
    }

    void setIsRunning(boolean isrunning) {
        this.isRunning.set(isrunning);
    }
    private WeakReference<DownloadTask> downloadTaskRef;
    private Class<T> classObject;
    private AtomicBoolean isCanceled = new AtomicBoolean(false);
    private AtomicBoolean isRunning = new AtomicBoolean(false);

   public JsonRequest(String url,String tag,Class<T> classObject,CallBack callBack){
       urlString =url;
       this.tag = tag;
       this.callBack = callBack;
       this.classObject = classObject;
       Fetcher fetcher = Fetcher.getInstance(null);
       JsonCache cache = fetcher.getJsonCache();
       String json = cache.getJson(url);
       if(json != null){
           Gson gson = new Gson();
           T a = gson.fromJson(json,classObject);
           callBack.onSuccess(0,a);
           return;
       }
       DownloadTask<T> task = new DownloadTask();
       downloadTaskRef = new WeakReference<DownloadTask>(task);
       setIsRunning(true);
       task.execute(this);


   }
    @Override
    public boolean cancel() {
        if(!isRunning.get())
            return  false;
        isRunning.set(false);
        isCanceled.set(true);
        Log.i(TAG, "Json download canceled..........");
        callBack.onFailure(-2, null);
        if(downloadTaskRef!= null && downloadTaskRef.get()!= null)
            return  downloadTaskRef.get().cancel(true);
        return  false;
    }

    static class DownloadTask<T> extends AsyncTask<JsonRequest<T>,Void,T> {
        private  JsonRequest<T> jsonRequest;
        private String jsonString;

        @Override
        protected T doInBackground(JsonRequest<T>... jsonRequests) {
            JsonRequest<T> request = jsonRequests[0];
            jsonRequest = request;
            String url = request.getUrlString();
            if(jsonRequest.isCanceled())
                return null;
            byte[] data =  downloadData(url,request.getIsCanceled());
            if(data != null){
                jsonString = new String(data/*, Charset.forName("UTF-8")*/);
                Log.i(TAG, "Downloaded json string= " + jsonString);
                Gson gson = new Gson();
                T a = gson.fromJson(jsonString,request.getClassObject());
                return  a;
            }
            return null;
        }

        @Override
        protected void onPostExecute(T t) {
            super.onPostExecute(t);
            CallBack callBack = jsonRequest.getCallBack();
            jsonRequest.setIsRunning(false);
            if(isCancelled()) {
                callBack.onFailure(-2,null);
                return;
            }
            if(t != null){
                JsonCache cache = Fetcher.getInstance(null).getJsonCache();
                cache.putJson(jsonRequest.getUrlString(),jsonString);
                Log.i(TAG, "test:" + jsonString);
                callBack.onSuccess(0,t);
            }else
                callBack.onFailure(-1,null);
        }

        byte[] downloadData(String urlst,AtomicBoolean canceled)  {
            InputStream input = null;
            HttpURLConnection connection = null;
            try {
                Log.i(TAG, "Downloading json url:" + urlst);
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
                }
                return Utils.toPrimitive(bytes.toArray(new Byte[]{}));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (connection != null)
                        connection.disconnect();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                    ignored.printStackTrace();
                }
            }
            return null;
        }
    }
}
