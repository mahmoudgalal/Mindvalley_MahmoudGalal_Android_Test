package com.mahmoudgalal.mindvalley_mahmoudgalal_android_test.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.mahmoudgalal.mindvalley_mahmoudgalal_android_test.fragments.ItemFragment;
import com.mahmoudgalal.mindvalley_mahmoudgalal_android_test.model.PinModel;

import java.util.List;

/**
 * Created by Mahmoud Galal on 01/10/2016.
 */
public class SwipeAdapter extends FragmentStatePagerAdapter {
    private List<PinModel> models;
    public SwipeAdapter(List<PinModel> models, FragmentManager fm) {
        super(fm);
        this.models = models;
    }

    @Override
    public Fragment getItem(int position) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        PinModel model = models.get(position);
        String userName = model.getUser().getName();
        String userProfileSmall  =  model.getUser().getProfileImage().getLarge();
        args.putString(ItemFragment.USER_NAME_KEY,userName);
        args.putString(ItemFragment.IMAGE_URL_KEY,userProfileSmall);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return models.size();
    }
}
