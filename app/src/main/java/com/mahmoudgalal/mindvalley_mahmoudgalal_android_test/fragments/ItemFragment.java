package com.mahmoudgalal.mindvalley_mahmoudgalal_android_test.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mahmoudgalal.mindvalley_mahmoudgalal_android_test.R;
import com.mahmoudgalal.mindvalley_mahmoudgalal_android_test.core.Fetcher;
import com.mahmoudgalal.mindvalley_mahmoudgalal_android_test.core.ImageRequest;
import com.mahmoudgalal.mindvalley_mahmoudgalal_android_test.core.Request;

import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class ItemFragment extends Fragment {
    public static final String USER_NAME_KEY = "USER_NAME_KEY";
    public static final String IMAGE_URL_KEY = "IMAGE_URL_KEY";
    private String userName;
    private String url;

    private ImageView imageView;
    private TextView userNameView;
    private ProgressBar progressBar;
    private ImageRequest imageRequest;

    public ItemFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_item, container, false);
        imageView  = (ImageView) root.findViewById(R.id.imageView2);
        userNameView  = (TextView) root.findViewById(R.id.textView2);
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
        Bundle bundle = getArguments();
        userName  = bundle.getString(USER_NAME_KEY);
        url  = bundle.getString(IMAGE_URL_KEY);
        userNameView.setText(userName);
        Fetcher fetcher = Fetcher.getInstance(getActivity());
        imageRequest = fetcher.newImageRequest(url, UUID.randomUUID().toString(), imageView,
                new Request.CallBack() {
            @Override
            public void onFailure(int code, Object extra) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity(),"Error loading image..!",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int code, Object extra) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        imageRequest.cancel();
    }
}
