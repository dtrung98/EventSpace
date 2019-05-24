package com.spacer.event.ui.main.tabs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spacer.event.R;
import com.spacer.event.util.Tool;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SpaceTab extends Fragment {
    private static final String TAG = "SpaceTab";

    @BindView(R.id.status_bar) View mStatusBar;

    public static SpaceTab newInstance() {
        SpaceTab fragment = new SpaceTab();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.space_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: ");
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        init();
    }

    public void init() {
        mStatusBar.getLayoutParams().height = Tool.getStatusHeight(getResources());
        mStatusBar.requestLayout();
    }
}
