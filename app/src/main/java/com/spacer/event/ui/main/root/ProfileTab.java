package com.spacer.event.ui.main.root;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.Group;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.spacer.event.R;
import com.spacer.event.ui.main.MainActivity;
import com.spacer.event.ui.main.page.inout.SignInFragment;
import com.spacer.event.ui.main.page.inout.SignUpFragment;
import com.spacer.event.util.Tool;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileTab extends Fragment {
    private static final String TAG = "ProfileTab";

    @BindView(R.id.status_bar) View mStatusBar;

    @BindView(R.id.not_signed_in_group)
    Group mNotSignedInGroup;

    @BindView(R.id.signed_in_group)
    Group mSignedInGroup;

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
        ButterKnife.bind(this, view);
        init();
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

    public void refreshData() {
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mUser==null) {
            mSignedInGroup.setVisibility(View.GONE);
            mNotSignedInGroup.setVisibility(View.VISIBLE);
        }
        else {
            mNotSignedInGroup.setVisibility(View.GONE);
            mSignedInGroup.setVisibility(View.VISIBLE);
        }
    }
}
