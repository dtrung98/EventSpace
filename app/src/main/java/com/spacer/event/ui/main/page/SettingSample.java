package com.spacer.event.ui.main.page;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spacer.event.R;
import com.spacer.event.ui.widget.fragmentnavigationcontroller.SupportFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingSample extends SupportFragment {
    private static final String TAG = "DashBoard";

    @OnClick(R.id.back_button)
    void back() {
        getMainActivity().dismiss();
    }

    @BindView(R.id.status_bar) View mStatusBar;


    public static SettingSample newInstance() {
       return new SettingSample();
    }

    @Nullable
    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.sample_page,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
    }

    @Override
    public void onSetStatusBarMargin(int value) {
        mStatusBar.getLayoutParams().height = value;
    }
}
