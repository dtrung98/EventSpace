package com.spacer.event.ui.main.page.inout;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.spacer.event.R;
import com.spacer.event.ui.widget.fragmentnavigationcontroller.PresentStyle;
import com.spacer.event.ui.widget.fragmentnavigationcontroller.SupportFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInFragment extends SupportFragment {
    public static final String TAG = "SignInFragment";

    public static SignInFragment newInstance() {

        Bundle args = new Bundle();

        SignInFragment fragment = new SignInFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.root) View mRoot;

    @OnClick({R.id.root,R.id.close})
    void back() {
        getNavigationController().dismissFragment();
    }



    /*@OnClick(R.id.back)
    void back() {
        getMainActivity().dismiss();
    }*/


    @Nullable
    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.sign_in,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
    }

    @OnClick(R.id.btn_google)
    void sigInWithGoogle() {

    }

    @OnClick(R.id.btn_facebook)
    void signInWithFaceBook() {

    }

    @OnClick(R.id.btn_sign_in)
    void signInWithForm() {

    }

    @Override
    public int defaultTransition() {
        return PresentStyle.ROTATE_DOWN_LEFT;
    }

    @Override
    public int defaultOpenExitTransition() {
        return PresentStyle.SLIDE_LEFT;
    }

}
