package com.spacer.event.ui.main;

import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.spacer.event.R;
import com.spacer.event.ui.main.root.BottomPagerAdapter;
import com.spacer.event.ui.widget.fragmentnavigationcontroller.PresentStyle;
import com.spacer.event.ui.widget.fragmentnavigationcontroller.SupportFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.VIBRATOR_SERVICE;

public class MainFragment extends SupportFragment implements BottomNavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {
    private static final String TAG = "MainFragment";

    @BindView(R.id.view_pager)
    ViewPager mBottomPager;

    @BindView(R.id.bottom_navigation_parent)
    View mBottomNavigationParent;

    @BindView(R.id.bottom_navigation_view)
    BottomNavigationView mBottomNavigationView;

    BottomPagerAdapter mBottomAdapter;

    @Nullable
    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.main_fragment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);

        vibrator  = (Vibrator) getMainActivity().getSystemService(VIBRATOR_SERVICE);

        mBottomAdapter = new BottomPagerAdapter(getMainActivity(),getChildFragmentManager());
        mBottomPager.setAdapter(mBottomAdapter);
        mBottomPager.setOffscreenPageLimit(3);
        mBottomPager.addOnPageChangeListener(this);

        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_trending:
                vibrate();
                mBottomPager.setCurrentItem(0);
                //  replaceFragment(mBottomAdapter.getItem(0),mBottomAdapter.getPageTitle(0).toString());
                return true;
            case R.id.navigation_cinema:
                vibrate();
                mBottomPager.setCurrentItem(1);
                // replaceFragment(mBottomAdapter.getItem(1),mBottomAdapter.getPageTitle(1).toString());
                return true;
            case R.id.navigation_profile:
                vibrate();
                mBottomPager.setCurrentItem(2);
                //  replaceFragment(mBottomAdapter.getItem(2),mBottomAdapter.getPageTitle(2).toString());
                return true;
        }
        return false;
    }

    Vibrator vibrator;
    private void vibrate() {
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(50);
        }
    }


    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }
    MenuItem prevMenuItem;

    @Override
    public void onPageSelected(int i) {
        vibrate();
        if(mBottomNavigationView!=null) {
            if (prevMenuItem != null)
                prevMenuItem.setChecked(false);
            else
                mBottomNavigationView.getMenu().getItem(0).setChecked(false);

            mBottomNavigationView.getMenu().getItem(i).setChecked(true);
            prevMenuItem = mBottomNavigationView.getMenu().getItem(i);
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public int defaultTransition() {
        return PresentStyle.FADE;
    }

}
