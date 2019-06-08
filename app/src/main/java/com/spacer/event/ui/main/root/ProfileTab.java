package com.spacer.event.ui.main.root;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.Group;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.auth.User;
import com.spacer.event.R;
import com.spacer.event.listener.FireBaseDocumentSetListener;
import com.spacer.event.ui.main.MainActivity;
import com.spacer.event.ui.main.page.inout.SignInFragment;
import com.spacer.event.ui.main.page.inout.SignUpFragment;
import com.spacer.event.util.SignInOutStatusChanged;
import com.spacer.event.util.Tool;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileTab extends Fragment implements SignInOutStatusChanged {
    private static final String TAG = "ProfileTab";

    @BindView(R.id.status_bar) View mStatusBar;

    @BindView(R.id.not_signed_in_panel)
    View mNotSignedInPanel;

    @BindView(R.id.signed_in_panel)
    View mSignedInPanel;

    @BindView(R.id.both_panel) View mBothPanel;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    FirebaseUser mUser = null;

    public static ProfileTab newInstance() {
        ProfileTab fragment = new ProfileTab();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: ");
        super.onViewCreated(view, savedInstanceState);
        if(getActivity() instanceof MainActivity)
            ((MainActivity)getActivity()).addSignInOutStatusListener(this);
        ButterKnife.bind(this, view);
        init();
        mSwipeRefresh.setOnRefreshListener(this::refreshData);
        refreshData();
    }

    public void init() {
        mStatusBar.getLayoutParams().height = Tool.getStatusHeight(getResources());
        mStatusBar.requestLayout();
    }

    @OnClick(R.id.sign_in)
    void goToSignIn() {
        if(getActivity() instanceof MainActivity)
            ((MainActivity)getActivity()).presentFragment(SignInFragment.newInstance());
    }

    @OnClick(R.id.sign_up)
    void goToSignUp() {
        if(getActivity() instanceof MainActivity)
            ((MainActivity)getActivity()).presentFragment(SignUpFragment.newInstance());

    }

    @OnClick(R.id.log_out_panel)
    void signOut() {
        Log.d(TAG, "signOut: u sign out");
        FirebaseAuth.getInstance().signOut();
        if(getActivity() instanceof MainActivity)
            ((MainActivity)getActivity()).justSignOut();
    }

    public void refreshData() {
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mUser==null) {
            Log.d(TAG, "refreshData: null user");
            mSignedInPanel.setVisibility(View.GONE);
            ((FrameLayout.LayoutParams)mBothPanel.getLayoutParams()).gravity = Gravity.CENTER;
            mNotSignedInPanel.setVisibility(View.VISIBLE);
        }
        else {
            Log.d(TAG, "refreshData: welcome "+mUser.getEmail());
            mNotSignedInPanel.setVisibility(View.GONE);
            ((FrameLayout.LayoutParams)mBothPanel.getLayoutParams()).gravity = Gravity.TOP;
            mSignedInPanel.setVisibility(View.VISIBLE);
            getProfile(mUser);
        }
        mSwipeRefresh.setRefreshing(false);
    }
    @BindView(R.id.name)
    TextView mName;

    @BindView(R.id.detail)
    TextView mDetail;

    @BindView(R.id.avatar)
    ImageView mAvatar;

    private void getProfile(FirebaseUser user) {
        String name = user.getDisplayName();
        if(name==null||name.isEmpty()) mName.setText("Unknown name");
        else mName.setText(name);
    }

    @Override
    public void onDestroyView() {
        if (getActivity() instanceof MainActivity)
            ((MainActivity)getActivity()).removeSignInOutListener(this);
        super.onDestroyView();
    }

    @Override
    public void onJustSignIn(FirebaseUser user) {
        Log.d(TAG, "onJustSignIn: user "+user.getEmail());
        refreshData();
    }

    @Override
    public void onJustSignOut() {
        refreshData();
    }
}
