package com.spacer.event.ui.main.page.inout;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spacer.event.R;
import com.spacer.event.ui.widget.fragmentnavigationcontroller.PresentStyle;
import com.spacer.event.ui.widget.fragmentnavigationcontroller.SupportFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpFragment extends SupportFragment {
    public static final String TAG = "SignUpFragment";

    public static SignUpFragment newInstance() {

        Bundle args = new Bundle();

        SignUpFragment fragment = new SignUpFragment();
        fragment.setArguments(args);
        return fragment;
    }

 /*   @OnClick(R.id.back)
    void back() {
        getMainActivity().dismiss();
    }*/

    @BindView(R.id.root) View mRoot;

    @OnClick({R.id.root,R.id.close})
    void back() {
        getNavigationController().dismissFragment();
    }

    @OnClick(R.id.sign_in)
    void goToSignIn() {
        getNavigationController().dismissFragment();
        getNavigationController().presentFragment(SignInFragment.newInstance());
    }


    @Nullable
    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.sign_up,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
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
