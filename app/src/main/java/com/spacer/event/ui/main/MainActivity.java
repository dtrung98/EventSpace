package com.spacer.event.ui.main;

import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.spacer.event.R;
import com.spacer.event.ui.main.tabs.BottomPagerAdapter;
import com.spacer.event.ui.widget.fragmentnavigationcontroller.BlankFragment;
import com.spacer.event.ui.widget.fragmentnavigationcontroller.FragmentNavigationController;
import com.spacer.event.ui.widget.fragmentnavigationcontroller.SupportFragment;
import com.spacer.event.util.Tool;


import butterknife.BindView;
import butterknife.ButterKnife;

import static com.spacer.event.ui.widget.fragmentnavigationcontroller.SupportFragment.PRESENT_STYLE_DEFAULT;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {
    private static final String TAG = "MainActivity";

    @BindView(R.id.view_pager)
    ViewPager mBottomPager;

    @BindView(R.id.bottom_navigation_parent)
    View mBottomNavigationParent;

    @BindView(R.id.bottom_navigation_view)
    BottomNavigationView mBottomNavigationView;

    BottomPagerAdapter mBottomAdapter;

    FragmentNavigationController mNavigationController;
    FirebaseAuth mAuth;
    public FirebaseUser user;

    @Override
    public void onBackPressed() {
        if(mNavigationController.getTopFragment().isReadyToDismiss())
            if(!(isNavigationControllerInit() && mNavigationController.dismissFragment(true)))
                super.onBackPressed();
    }

    private boolean isNavigationControllerInit() {
        return null!= mNavigationController;
    }
    public void presentFragment(SupportFragment fragment) {
        if(isNavigationControllerInit()) {
//            Random r = new Random();
//            mNavigationController.setPresentStyle(r.nextInt(39)+1); //exclude NONE present style
            mNavigationController.setPresentStyle(fragment.getPresentTransition());
            mNavigationController.presentFragment(fragment, true);
        }
    }

    private void initBackStack(Bundle savedInstanceState) {
        FragmentManager fm = getSupportFragmentManager();
        mNavigationController = FragmentNavigationController.navigationController(fm, R.id.container);
        mNavigationController.setPresentStyle(PRESENT_STYLE_DEFAULT);
        mNavigationController.setDuration(250);
        mNavigationController.setInterpolator(new AccelerateDecelerateInterpolator());
        mNavigationController.presentFragment(new BlankFragment());
    }

    public void dismiss() {
        if(isNavigationControllerInit()) {
            mNavigationController.dismissFragment();
        }
    }

    public void presentFragment(SupportFragment fragment, boolean animated) {
        if(isNavigationControllerInit()) {
            mNavigationController.presentFragment(fragment,animated);
        }
    }
    public void dismiss(boolean animated) {
        if(isNavigationControllerInit()) {
            mNavigationController.dismissFragment(animated);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                dismiss();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Tool.init(this);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN );
        ButterKnife.bind(this);

        vibrator  = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        mBottomAdapter = new BottomPagerAdapter(this,getSupportFragmentManager());
        mBottomPager.setAdapter(mBottomAdapter);
        mBottomPager.setOffscreenPageLimit(3);
        mBottomPager.addOnPageChangeListener(this);

        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
        initBackStack(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_trending:
                mBottomPager.setCurrentItem(0);
                //  replaceFragment(mBottomAdapter.getItem(0),mBottomAdapter.getPageTitle(0).toString());
                return true;
            case R.id.navigation_cinema:
                mBottomPager.setCurrentItem(1);
                // replaceFragment(mBottomAdapter.getItem(1),mBottomAdapter.getPageTitle(1).toString());
                return true;
            case R.id.navigation_profile:
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
    protected void onDestroy() {
        Tool.destroy();
        super.onDestroy();
    }
}
