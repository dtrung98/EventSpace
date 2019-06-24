package com.spacer.event.ui.main.page.eventspace;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.motion.MotionLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class SpaceDetailFragment extends SupportFragment implements EventAdapter.EventTypeListener {
    private static final String TAG = "SpaceDetailFragment";


    public static SpaceDetailFragment newInstance(List<Space> spaces, List<EventType> eventTypes, Space space) {
        SpaceDetailFragment fragment = new SpaceDetailFragment();
        if(spaces!=null) fragment.mSpaces.addAll(spaces);
        if(eventTypes!=null) fragment.mEventTypes.addAll(eventTypes);
        if(space!=null) fragment.mSpace = space;
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

    @BindView(R.id.image)
    ImageView mImage;

    @BindView(R.id.status_bar) View mStatusBar;

    ArrayList<Space> mSpaces = new ArrayList<>();
    ArrayList<EventType> mEventTypes = new ArrayList<>();
    ArrayList<Service> mServices = new ArrayList<>();

    Space mSpace;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    EventAdapter mAdapter;

    @BindView(R.id.root)
    MotionLayout mMotionLayout;

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

        mImage.setVisibility(View.VISIBLE);

        mAdapter = new EventAdapter(getActivity());
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
        if(mSpacesTask !=null) mSpacesTask.cancel();
        super.onDestroyView();
    }

    private CountingSpacesTask mSpacesTask;

    private void refreshData() {
        if(mSpace!=null) {

            mTitle.setText(mSpace.getName());
            mAdapter.setSpace(mSpace,false);

            if(!mSpace.getImages().isEmpty())
                Glide.with(this)
                        .load(mSpace.getImages().get(0))
                        .into(mImage);


            if (mSpacesTask != null) mSpacesTask.cancel();
            mSpacesTask = new CountingSpacesTask(this);
            mSpacesTask.execute();

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

    public void reportCountResult(ArrayList<Integer> counts,boolean done) {
        mAdapter.setCountData(counts,done);
        if(done)
            mSwipeRefresh.setRefreshing(false);
    }

    public void reportEventResult(ArrayList<EventType> data, boolean done) {
        mAdapter.setData(data, done);
        if(done)
            mSwipeRefresh.setRefreshing(false);
    }

    @Override
    public void onEventItemClick(EventType eventType) {
        getNavigationController().presentFragment(BookingFragment.newInstance(mSpace,eventType));
    }

    private static class CountingSpacesTask extends AsyncTask<Void,Void,Void> {
        WeakReference<SpaceDetailFragment> mWFragment ;
        public CountingSpacesTask(SpaceDetailFragment fragment) {
            mWFragment = new WeakReference<>(fragment);
        }
        public void cancel() {
            cancel(true);
            if(mWFragment.get()!=null) mWFragment.get().mSpacesTask = null;
            mWFragment.clear();
            mWFragment = null;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            if (mWFragment.get() != null) {
                ArrayList<EventType> allEvents = mWFragment.get().mEventTypes;
                Space space = mWFragment.get().mSpace;

                if(space!=null&&allEvents!=null) {

                    ArrayList<EventType> eventsOfSpace = new ArrayList<>();
                    ArrayList<String> staticNames = space.getSupportEvents();
                    int size = space.getSupportEvents().size();
                    for (int i = 0; i < size; i++) {
                        String staticName = staticNames.get(i);

                        for (EventType event :
                                allEvents) {
                            if (event != null && event.equals(staticName)) {
                                eventsOfSpace.add(event);
                                break;
                            } else
                                Log.d(TAG, "doInBackground: "+event+" vs "+ staticName);
                        }

                    }

                    if (mWFragment.get() != null) {
                        mWFragment.get().mSwipeRefresh.post(() -> mWFragment.get().reportEventResult(eventsOfSpace, true));
                    }
                }
            }
            if(true) return null;
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
                mWFragment.get().mSwipeRefresh.post(() -> mWFragment.get().reportCountResult(counts,true));
            }

            return null;
        }
    }
}
