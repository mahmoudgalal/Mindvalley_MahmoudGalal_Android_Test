package com.mahmoudgalal.mindvalley_mahmoudgalal_android_test.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mahmoudgalal.mindvalley_mahmoudgalal_android_test.R;
import com.mahmoudgalal.mindvalley_mahmoudgalal_android_test.adapters.SwipeAdapter;
import com.mahmoudgalal.mindvalley_mahmoudgalal_android_test.model.PinModel;

import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 */
public class SwipeFragment extends Fragment {

    private ViewPager pager;
    private SwipeAdapter adapter;
    public static final String DATA_KEY = "DATA_KEY";
    public SwipeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_swipe, container, false);
        Bundle args = getArguments();
        PinModel[] models = (PinModel[]) args.getParcelableArray(DATA_KEY);
        adapter = new SwipeAdapter(Arrays.asList(models),getFragmentManager());
        pager = (ViewPager) root.findViewById(R.id.pager);
        pager.setAdapter(adapter);
        return  root;
    }


}
