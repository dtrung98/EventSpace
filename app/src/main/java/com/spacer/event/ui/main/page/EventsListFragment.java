package com.spacer.event.ui.main.page;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
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

public class EventsListFragment extends SupportFragment{

    public static EventsListFragment newInstance(List<Space> spaces, List<EventType> eventTypes) {
        EventsListFragment fragment = new EventsListFragment();
        if(spaces!=null) fragment.mSpaces.addAll(spaces);
        if(eventTypes!=null) fragment.mEventTypes.addAll(eventTypes);
        return fragment;
    }

    @OnClick(R.id.back)
    void back() {
        getMainActivity().dismiss();
    }

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    ArrayList<Space> mSpaces = new ArrayList<>();
    ArrayList<EventType> mEventTypes = new ArrayList<>();
    ArrayList<Service> mServices = new ArrayList<>();


    @BindView(R.id.status_bar) View mStatusBar;

    @Nullable
    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.event_list_fragment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mSwipeRefresh.setOnRefreshListener(this::refreshData);
        mSwipeRefresh.setRefreshing(true);
        refreshData();
    }

    private void refreshData() {
        mSwipeRefresh.setRefreshing(false);
    }

   /* @Override
    public int defaultPresentStyle() {
        return PresentStyle.SLIDE_UP;
    }*/

    @Override
    public void onSetStatusBarMargin(int value) {
       mStatusBar.getLayoutParams().height=value;
       mStatusBar.requestLayout();
    }
}
