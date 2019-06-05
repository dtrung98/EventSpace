package com.spacer.event.ui.main.page;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spacer.event.R;
import com.spacer.event.ui.widget.fragmentnavigationcontroller.PresentStyle;
import com.spacer.event.ui.widget.fragmentnavigationcontroller.SupportFragment;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SamplePageTwo extends SupportFragment {
    private static final String TAG = "SamplePageTwo";

    @OnClick(R.id.back)
    void back() {
        getMainActivity().dismiss();
    }
    @OnClick(R.id.button)
    void goToSomeWhere() {
        getMainActivity().presentFragment(new SamplePageThree());
    }

    @BindView(R.id.root) View mRoot;

    public static SamplePageTwo newInstance(int value) {
        SamplePageTwo fragment = new SamplePageTwo();
        fragment.p = value;
        return fragment;
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
        mRoot.setBackgroundResource(R.color.FlatGreen);
    }

    int p = -1;
    @Override
    public int defaultTransition() {
        if(p==-1) {
            Random r = new Random();
            p = r.nextInt(39) + 1; //exclude NONE present style
        }
        return p;
    }
}
