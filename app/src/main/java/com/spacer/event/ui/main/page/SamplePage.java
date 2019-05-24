package com.spacer.event.ui.main.page;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spacer.event.R;
import com.spacer.event.ui.widget.fragmentnavigationcontroller.SupportFragment;

public class SamplePage extends SupportFragment {
    private static final String TAG = "SamplePage";

    @Nullable
    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.sample_page,container,false);
    }
}
