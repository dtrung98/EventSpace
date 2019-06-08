package com.spacer.event.ui.main;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.spacer.event.R;
import com.spacer.event.ui.widget.fragmentnavigationcontroller.FragmentNavigationController;
import com.spacer.event.ui.widget.fragmentnavigationcontroller.PresentStyle;
import com.spacer.event.ui.widget.fragmentnavigationcontroller.SupportFragment;
import com.spacer.event.util.SignInOutStatusChanged;
import com.spacer.event.util.Tool;


import java.util.ArrayList;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity   {
    private static final String TAG = "MainActivity";

    FragmentNavigationController mNavigationController;
    FirebaseAuth mAuth;
    public FirebaseUser user;

    public void justSignIn(FirebaseUser user) {
        for (SignInOutStatusChanged listener :
                mSignInOutListeners) {
            listener.onJustSignIn(user);
        }
    }

    public void justSignOut() {
        for (SignInOutStatusChanged listener :
                mSignInOutListeners) {
            listener.onJustSignOut();
        }
    }

    private ArrayList<SignInOutStatusChanged> mSignInOutListeners = new ArrayList<>();

    public void addSignInOutStatusListener(SignInOutStatusChanged listener) {
        mSignInOutListeners.add(listener);
    }

    public void removeSignInOutListener(SignInOutStatusChanged listener) {
        mSignInOutListeners.remove(listener);
    }


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

            mNavigationController.presentFragment(fragment, true);
        }
    }

    private void initBackStack(Bundle savedInstanceState) {
        FragmentManager fm = getSupportFragmentManager();
        mNavigationController = FragmentNavigationController.navigationController(fm, R.id.container);
        mNavigationController.setInterpolator(new AccelerateDecelerateInterpolator());
        mNavigationController.presentFragment(new MainFragment());
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


        initBackStack(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

    }


    @Override
    protected void onDestroy() {
        Tool.destroy();
        super.onDestroy();
    }
}
