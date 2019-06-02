package com.spacer.event.ui.main.page;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.spacer.event.R;
import com.spacer.event.model.EventType;
import com.spacer.event.model.Service;
import com.spacer.event.model.Space;
import com.spacer.event.ui.widget.fragmentnavigationcontroller.PresentStyle;
import com.spacer.event.ui.widget.fragmentnavigationcontroller.SupportFragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.AEADBadTagException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventListFragment extends SupportFragment{

    public static EventListFragment newInstance(List<Space> spaces, List<EventType> eventTypes) {
        EventListFragment fragment = new EventListFragment();
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

 @BindView(R.id.back) View mBack;
 @BindView(R.id.title)
    TextView mTitle;

    ArrayList<Space> mSpaces = new ArrayList<>();
    ArrayList<EventType> mEventTypes = new ArrayList<>();
    ArrayList<Service> mServices = new ArrayList<>();

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    EventAdapter mAdapter;


    @Nullable
    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.event_list_fragment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);


        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        mAdapter = new EventAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefresh.setOnRefreshListener(this::refreshData);
        mSwipeRefresh.setProgressViewOffset(true,mSwipeRefresh.getProgressViewStartOffset(),mSwipeRefresh.getProgressViewEndOffset());
        mSwipeRefresh.setColorSchemeResources(R.color.FlatOrange);
        mSwipeRefresh.setRefreshing(true);

        refreshData();
    }

    @Override
    public void onDestroyView() {
        if(mCountingSpacesTask!=null) mCountingSpacesTask.cancel();
        super.onDestroyView();
    }

    private CountingSpacesTask mCountingSpacesTask;

    private void refreshData() {
        mSwipeRefresh.setRefreshing(false);
        mAdapter.setData(mEventTypes,false);
        if(mCountingSpacesTask !=null) mCountingSpacesTask.cancel();
        mCountingSpacesTask = new CountingSpacesTask(this);
        mCountingSpacesTask.execute();
    }

    @Override
    public int defaultPresentStyle() {
        return PresentStyle.SLIDE_UP;
    }

    @Override
    public void onSetStatusBarMargin(int value) {
        ((ViewGroup.MarginLayoutParams)mBack.getLayoutParams()).topMargin = (int) (getResources().getDimension(R.dimen.dp_16) +value);
        ((ViewGroup.MarginLayoutParams)mTitle.getLayoutParams()).topMargin += value;
    }

    private class CountingSpacesTask extends AsyncTask<Void,Void,Void> {
        WeakReference<EventListFragment> mWFragment ;
        public CountingSpacesTask(EventListFragment fragment) {
            mWFragment = new WeakReference<>(fragment);
        }
        public void cancel() {
            cancel(true);
            if(mWFragment.get()!=null) mWFragment.get().mCountingSpacesTask = null;
            mWFragment.clear();
            mWFragment = null;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ArrayList<Integer> counts = new ArrayList<>();

            if(mWFragment.get()!=null) {
                ArrayList<Space> spaces = mWFragment.get().mSpaces;
                ArrayList<EventType> eventTypes = mWFragment.get().mEventTypes;


                int size = eventTypes.size();
                for (int i = 0; i <size; i++) {
                    EventType eventType = eventTypes.get(i);
                    int count = 0;
                    for (Space space : spaces) {
                        if(space.getSupportEvents().contains(eventType.getStaticName())) count ++;
                    }
                    counts.add(count);
                }
            }

            if(mWFragment.get()!=null) {
                mWFragment.get().mSwipeRefresh.post(() -> mWFragment.get().mAdapter.setCountData(counts,true));
            }

            return null;
        }
    }
}
