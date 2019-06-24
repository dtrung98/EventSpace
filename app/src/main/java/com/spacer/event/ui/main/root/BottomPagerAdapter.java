package com.spacer.event.ui.main.root;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.spacer.event.R;
import com.spacer.event.ui.main.root.spacetab.SpaceTab;
import com.spacer.event.ui.main.root.trending.TrendingTab;

import java.util.ArrayList;

public class BottomPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;

    public BottomPagerAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        mContext = context;
        initData();
    }

    private ArrayList<Fragment> mData = new ArrayList<>();

    private void initData() {
        mData.add(TrendingTab.newInstance());
        mData.add(SpaceTab.newInstance());
        mData.add(ProfileTab.newInstance());
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return mData.size();
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        return mData.get(position);
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getResources().getString(R.string.trending);
            case 1:
                return mContext.getResources().getString(R.string.space);
            case 2:
                return mContext.getResources().getString(R.string.account_settings);
            default:
                return null;
        }
    }
}