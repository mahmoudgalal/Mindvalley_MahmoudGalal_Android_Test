package com.mahmoudgalal.mindvalley_mahmoudgalal_android_test;


import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.mahmoudgalal.mindvalley_mahmoudgalal_android_test.core.Fetcher;
import com.mahmoudgalal.mindvalley_mahmoudgalal_android_test.core.JsonRequest;
import com.mahmoudgalal.mindvalley_mahmoudgalal_android_test.core.Request;
import com.mahmoudgalal.mindvalley_mahmoudgalal_android_test.core.utils.Utils;
import com.mahmoudgalal.mindvalley_mahmoudgalal_android_test.fragments.StartFragment;
import com.mahmoudgalal.mindvalley_mahmoudgalal_android_test.fragments.SwipeFragment;
import com.mahmoudgalal.mindvalley_mahmoudgalal_android_test.model.PinModel;

import java.util.Arrays;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {
    private static  final String TAG = MainActivity.class.getSimpleName();
    private  static final String URL_TEST_JSON ="http://pastebin.com/raw/wgkJgazE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StartFragment itemFragment = new StartFragment();
        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container,itemFragment,"StartFragment")
                .commit();
        if(!Utils.isInternetConnectionExist(this)){
            //No internet connection !!Loading from cache?
        }
        Fetcher fetcher = Fetcher.getInstance(this.getApplicationContext());
        JsonRequest<PinModel[]> jsonRequest = fetcher.newJsonRequest(URL_TEST_JSON,
                UUID.randomUUID().toString(),PinModel[].class , new Request.CallBack() {
                    @Override
                    public void onFailure(int code, Object extra) {
                        Log.i(TAG,"onFailure ....");
                        Toast.makeText(MainActivity.this,"Error downloading data...",
                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onSuccess(int code, Object extra) {
                        Log.i(TAG,"onSuccess ....");
                        PinModel[] arr = ((PinModel[])extra);
                        String str = arr.length+"  - "+ Arrays.toString(arr);
                        Log.i(TAG, str);

                        SwipeFragment swipeFragment =  new SwipeFragment();
                        Bundle args =  new Bundle();
                        args.putParcelableArray(SwipeFragment.DATA_KEY,arr);
                        swipeFragment.setArguments(args);

                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .setCustomAnimations(
                                        R.anim.slide_left_enter,
                                        R.anim.slide_left_exit,
                                        R.anim.detail_fragment_enter,
                                        R.anim.detail_fragment_pop_exit)
                                .replace(R.id.container,swipeFragment,"SwiprFragment")
                                .commit();
                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Fetcher.getInstance(null).clearCache();
    }
}
