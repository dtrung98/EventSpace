package com.spacer.event.ui.main.page.eventspace;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.spacer.event.R;
import com.spacer.event.model.EventType;
import com.spacer.event.model.Service;
import com.spacer.event.model.Space;
import com.spacer.event.ui.main.page.booking.BookingFragment;
import com.spacer.event.ui.widget.fragmentnavigationcontroller.PresentStyle;
import com.spacer.event.ui.widget.fragmentnavigationcontroller.SupportFragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventDetailFragment extends SupportFragment implements SpaceInEventAdapter.SpaceListener {
    private static final String TAG = "EventDetailFragment";

    public static EventDetailFragment newInstance(List<Space> spaces, List<EventType> eventTypes, EventType event) {
        EventDetailFragment fragment = new EventDetailFragment();
        if(spaces!=null) fragment.mSpaces.addAll(spaces);
        if(eventTypes!=null) fragment.mEventTypes.addAll(eventTypes);
        if(event!=null) fragment.mEventType = event;
        return fragment;
    }

    @OnClick(R.id.back_button)
    void back() {
        getMainActivity().dismiss();
    }

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    @BindView(R.id.back_button) View mBack;
    @BindView(R.id.title)
    TextView mTitle;

    @BindView(R.id.icon)
    ImageView mIcon;

    @BindView(R.id.status_bar) View mStatusBar;

    ArrayList<Space> mSpaces = new ArrayList<>();
    ArrayList<EventType> mEventTypes = new ArrayList<>();
    ArrayList<Service> mServices = new ArrayList<>();

    EventType mEventType;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    SpaceInEventAdapter mAdapter;

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Nullable
    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.space_detail,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mIcon.setVisibility(View.VISIBLE);
        mAdapter = new SpaceInEventAdapter(getActivity());
        mAdapter.setListener(this);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),mAdapter.getSpanCount());
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                return mAdapter.getSpanSizeItem(i);
            }
        });
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mSwipeRefresh.setEnabled(false);
        mSwipeRefresh.setOnRefreshListener(this::refreshData);
        mSwipeRefresh.setProgressViewOffset(true,mSwipeRefresh.getProgressViewStartOffset(),mSwipeRefresh.getProgressViewEndOffset());
        mSwipeRefresh.setColorSchemeResources(R.color.FlatOrange);
        mSwipeRefresh.setRefreshing(true);

        refreshData();
    }

    @Override
    public void onDestroyView() {
        if(mLoadSpacesTask !=null) mLoadSpacesTask.cancel();
        super.onDestroyView();
    }

    private LoadSpacesTask mLoadSpacesTask;

    private void refreshData() {
        if(mEventType!=null) {

            mTitle.setText(mEventType.getName());
            mAdapter.setEventType(mEventType,false);

           if(!mEventType.getIcon().isEmpty())
                Glide.with(this)
                        .load(mEventType.getIcon())
                        .into(mIcon);

            if (mLoadSpacesTask != null) mLoadSpacesTask.cancel();
            mLoadSpacesTask = new LoadSpacesTask(this);
            mLoadSpacesTask.execute();

        } else mSwipeRefresh.setRefreshing(false);
    }

    @Override
    public int defaultTransition() {
        return PresentStyle.SLIDE_LEFT;
    }

    @Override
    public int defaultOpenExitTransition() {
        return PresentStyle.ACCORDION_LEFT;
    }

    @Override
    public void onSetStatusBarMargin(int value) {
        mStatusBar.getLayoutParams().height = value;
    }


    public void reportResult(ArrayList<Space> data, boolean done) {
        mAdapter.setData(data, done);
        if(done)
            mSwipeRefresh.setRefreshing(false);
    }

    @Override
    public void onSpaceItemClick(Space space) {
        getNavigationController().presentFragment(BookingFragment.newInstance(space,mEventType));
    }

    private static class LoadSpacesTask extends AsyncTask<Void,Void,Void> {
        WeakReference<EventDetailFragment> mWFragment ;
        public LoadSpacesTask(EventDetailFragment fragment) {
            mWFragment = new WeakReference<>(fragment);
        }
        public void cancel() {
            cancel(true);
            if(mWFragment.get()!=null) mWFragment.get().mLoadSpacesTask = null;
            mWFragment.clear();
            mWFragment = null;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            if (mWFragment.get() != null) {
                ArrayList<Space> allSpaces = mWFragment.get().mSpaces;
                EventType eventType = mWFragment.get().mEventType;

                if(eventType!=null&&allSpaces!=null) {

                    ArrayList<Space> spaceHasThatEvent = new ArrayList<>();
                    String staticName = eventType.getStaticName();

                    int allSpaceSize = allSpaces.size();
                    for (int i = 0; i < allSpaceSize; i++) {
                        int pos = allSpaces.get(i).getSupportEvents().indexOf(staticName);
                        if(pos!=-1)
                            spaceHasThatEvent.add(allSpaces.get(i));
                    }

                    if (mWFragment.get() != null) {
                        mWFragment.get().mSwipeRefresh.post(() -> mWFragment.get().reportResult(spaceHasThatEvent, true));
                    }
                }
            }
            return null;
        }
    }
}
