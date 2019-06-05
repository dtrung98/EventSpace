package com.spacer.event.ui.main.page;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spacer.event.R;
import com.spacer.event.model.EventType;
import com.spacer.event.model.Service;
import com.spacer.event.model.Space;
import com.spacer.event.ui.widget.fragmentnavigationcontroller.PresentStyle;
import com.spacer.event.ui.widget.fragmentnavigationcontroller.SupportFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchFragment extends SupportFragment implements SearchView.OnQueryTextListener {

    public static SearchFragment newInstance(List<Space> spaces, List<EventType> eventTypes) {
        SearchFragment fragment = new SearchFragment();
        if(spaces!=null) fragment.mSpaces.addAll(spaces);
        if(eventTypes!=null) fragment.mEventTypes.addAll(eventTypes);
        return fragment;
    }

    @OnClick(R.id.back)
    void back() {
        getMainActivity().dismiss();
    }

    ArrayList<Space> mSpaces = new ArrayList<>();
    ArrayList<EventType> mEventTypes = new ArrayList<>();
    ArrayList<Service> mServices = new ArrayList<>();

    @BindView(R.id.search_view)
    SearchView mSearchView;

    @BindView(R.id.search_panel)
    View mSearchPanel;

    @BindView(R.id.root) View mRoot;

    @BindView(R.id.status_bar) View mStatusBar;

    @Nullable
    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.search_fragment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mSearchView.setOnQueryTextListener(this);
    }

    @Override
    public int defaultTransition() {
        return PresentStyle.FADE;
    }

    @Override
    public int defaultOpenExitTransition() {
        return PresentStyle.REMOVED_FRAGMENT_PRESENT_STYLE;
    }

    @Override
    public void onResume() {
        super.onResume();
        mSearchView.onActionViewExpanded();
        mSearchView.requestFocus();
    }

    @Override
    public void onPause() {
        mSearchView.clearFocus();
        super.onPause();
    }

    @Override
    public void onSetStatusBarMargin(int value) {
       mStatusBar.getLayoutParams().height=value;
       mStatusBar.requestLayout();
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return true;
    }
}
