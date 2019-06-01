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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchPage extends SupportFragment {
    private static final String TAG = "SamplePage";

    public static SearchPage newInstance() {

        SearchPage fragment = new SearchPage();

        return fragment;
    }

    @OnClick(R.id.back)
    void back() {
        getMainActivity().dismiss();
    }
    @OnClick(R.id.button)
    void goToSomeWhere() {
        getMainActivity().presentFragment(new SamplePageThree());
    }

    @BindView(R.id.root) View mRoot;

    @Nullable
    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.sample_page,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        mRoot.setBackgroundResource(R.color.DarkerBlack);
    }

    @Override
    public int defaultPresentStyle() {
        return PresentStyle.SLIDE_UP;
    }

    @Override
    public boolean useOpenAsExitPresentStyle() {
        return false;
    }
}
