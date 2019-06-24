package com.spacer.event.ui.main.root.trending;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.spacer.event.R;
import com.spacer.event.listener.FireBaseCollectionListener;
import com.spacer.event.model.EventSpace;
import com.spacer.event.model.EventSpaceTrending;
import com.spacer.event.model.EventType;
import com.spacer.event.model.Index;
import com.spacer.event.model.Space;
import com.spacer.event.ui.main.MainActivity;
import com.spacer.event.ui.main.page.booking.BookingBottomSheet;
import com.spacer.event.ui.main.page.booking.BookingFragment;
import com.spacer.event.ui.main.page.eventspace.EventDetailFragment;
import com.spacer.event.ui.main.page.eventspace.SpaceDetailFragment;
import com.spacer.event.ui.main.page.search.SearchAdapter;
import com.spacer.event.ui.main.page.search.SearchFragment;
import com.spacer.event.util.Tool;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TrendingTab extends Fragment  implements AppBarLayout.OnOffsetChangedListener, SearchAdapter.TypeListener {
    private static final String TAG = "TrendingTab";

    public static TrendingTab newInstance() {
        return new TrendingTab();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.trending_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: ");
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        init();
        mSwipeRefresh.setOnRefreshListener(this::refreshData);
        mSwipeRefresh.setRefreshing(true);
        refreshData();
    }
    @BindView(R.id.toolbar_parent) View mToolbarParent;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.error)
    View mError;

    TrendingAdapter mAdapter;

    @OnClick(R.id.search)
    void goToSearch() {
        if(getActivity() instanceof MainActivity)
            ((MainActivity)getActivity()).presentFragment(SearchFragment.newInstance(mSpaces,mEvents));
    }

    public void init() {
        int statusHeight = Tool.getStatusHeight(getResources());
        if (statusHeight != (int) getResources().getDimension(R.dimen.default_status_height)) {
            mToolbarParent.setPadding(0, statusHeight, 0, 0);
        }
        mSwipeRefresh.setColorSchemeResources(R.color.FlatOrange);
        mSwipeRefresh.setProgressViewOffset(true,mSwipeRefresh.getProgressViewStartOffset()+(int)getResources().getDimension(R.dimen.swipe_top_off_set),mSwipeRefresh.getProgressViewEndOffset()+(int)getResources().getDimension(R.dimen.swipe_top_off_set));

        mAdapter = new TrendingAdapter();
        mAdapter.setListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        mRecyclerView.setAdapter(mAdapter);
    }

    public void refreshData() {
        mRefreshResult[0] = mRefreshResult[1] = mRefreshResult[2] = UN_SET;
        FirebaseFirestore.getInstance()
                .collection("indexs")
                .get()
                .addOnSuccessListener(mIndexListener)
                .addOnFailureListener(mIndexListener);

        FirebaseFirestore.getInstance()
                .collection("event_types")
                .get()
                .addOnSuccessListener(mEventTypeListener)
                .addOnFailureListener(mEventTypeListener);

        FirebaseFirestore.getInstance()
                .collection("spaces")
                .get()
                .addOnSuccessListener(mSpaceListener)
                .addOnFailureListener(mSpaceListener);
    }
    public List<EventType> mEvents = new ArrayList<>();
    public List<Space> mSpaces = new ArrayList<>();
    public List<Index> mIndexList = new ArrayList<>();
    public int[] mRefreshResult = new int[3];

    private static int UN_SET = 0;
    private static int SUCCESS = 1;
    private static int FAILURE = 2;

    public void checkResult() {
        Log.d(TAG, "checkResult: [0] = "+mRefreshResult[0]+", [1] = "+mRefreshResult[1] +", [2] = "+mRefreshResult[2]);
        if(mRefreshResult[0]==UN_SET ||mRefreshResult[1]==UN_SET ||mRefreshResult[2]==UN_SET) return;

        mSwipeRefresh.setRefreshing(false);

        if(mRefreshResult[0]==SUCCESS&&mRefreshResult[1]==SUCCESS&&mRefreshResult[2]==SUCCESS) {

            Log.d(TAG, "checkResult: index size = "+ mIndexList.size()+", events size = "+mEvents.size());
            if(mTask!=null) mTask.cancel();
            mTask = new TrendingTask(this);
            mTask.execute();
        } else {
            mSwipeRefresh.setRefreshing(false);
            mError.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private FireBaseCollectionListener mIndexListener = new FireBaseCollectionListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            mRefreshResult[0] = FAILURE;
            checkResult();
        }

        @Override
        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
            mIndexList = queryDocumentSnapshots.toObjects(Index.class);
            mRefreshResult[0] = SUCCESS;
            checkResult();
        }
    };

    private FireBaseCollectionListener mEventTypeListener = new FireBaseCollectionListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            mRefreshResult[1] = FAILURE;
            checkResult();
        }

        @Override
        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
            mEvents = queryDocumentSnapshots.toObjects(EventType.class);
            mRefreshResult[1] = SUCCESS;
            checkResult();
        }
    };

    private FireBaseCollectionListener mSpaceListener = new FireBaseCollectionListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            mRefreshResult[2] = FAILURE;
            checkResult();
        }

        @Override
        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
            mSpaces = queryDocumentSnapshots.toObjects(Space.class);
            mRefreshResult[2] = SUCCESS;
            checkResult();
        }
    };

    @BindView(R.id.app_bar)
    AppBarLayout mAppBarLayout;

    @Override
    public void onResume() {
        super.onResume();
        mAppBarLayout.addOnOffsetChangedListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mAppBarLayout.removeOnOffsetChangedListener(this);
    }
    TrendingTask mTask;

    @Override
    public void onDestroyView() {
        if(mTask!=null) mTask.cancel();
        mAdapter.removeListener();

        super.onDestroyView();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        mSwipeRefresh.setEnabled(i==0||mSwipeRefresh.isRefreshing());
    }

    @Override
    public void onTypeItemClick(Object object) {
        if(getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity)getActivity();
            if (object instanceof EventType) {
                activity.presentFragment(EventDetailFragment.newInstance(mSpaces,mEvents,(EventType) object));
            } else if (object instanceof Space) {
                activity.presentFragment(SpaceDetailFragment.newInstance(mSpaces,mEvents,(Space)object));
            } else if(object instanceof EventSpaceTrending) {
                EventSpaceTrending es = (EventSpaceTrending)object;
              /*  BookingBottomSheet bookingBottomSheet = BookingBottomSheet.newInstance(es.space,es.event);
                bookingBottomSheet.show(getChildFragmentManager(),"BookingBottomSheet");*/
              activity.presentFragment(BookingFragment.newInstance(es.space,es.event));
            }
        }
    }

    private static class TrendingTask extends AsyncTask<Void,Void,Void> {
        WeakReference<TrendingTab> mWFragment;

        TrendingTask(TrendingTab fragment) {
            mWFragment = new WeakReference<>(fragment);
        }

        void cancel() {
            cancel(true);
            if (mWFragment.get() != null) mWFragment.get().mTask = null;
            mWFragment.clear();
            mWFragment = null;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ArrayList<List<Object>> objectList = new ArrayList<>();

            if (mWFragment.get() != null) {
                List<Space> spaces = mWFragment.get().mSpaces;
                List<EventType> eventTypes = mWFragment.get().mEvents;
                List<Index> indexList = mWFragment.get().mIndexList;

                int size = indexList.size();
                Collections.sort(indexList, new Comparator<Index>() {
                    @Override
                    public int compare(Index o1, Index o2) {
                        return o1.getIndex() - o2.getIndex();
                    }
                });

                for (Index index :
                        indexList) {
                    if (index.getType().equals(Index.EVENT))
                      objectList.add(loadEvent(spaces, eventTypes, index));
                    else if (index.getType().equals(Index.SPACE))
                        objectList.add(loadSpace(spaces, eventTypes, index));
                    else objectList.add(loadEventSpace(spaces, eventTypes, index));
                }
            }

            if (mWFragment.get() != null) {
                mWFragment.get().mSwipeRefresh.post(() -> mWFragment.get().setData(objectList));
            }

            return null;
        }

        private List<Object> loadEvent(List<Space> allSpaces, List<EventType> allEvents, Index index) {
            ArrayList<String> data = index.getData();
            ArrayList<Object> events = new ArrayList<>();

            for (String name : data) {
                for (EventType event :
                        allEvents) {
                    if (event != null && event.getStaticName().equals(name)) {
                        events.add(event);
                        break;
                    }
                }
            }
            return events;
        }

        private List<Object> loadSpace(List<Space> allSpaces, List<EventType> allEvents, Index index) {
            ArrayList<String> data = index.getData();
            ArrayList<Object> spaces = new ArrayList<>();

            for (String id : data) {
                for (Space space :
                        allSpaces) {
                    if (id != null && id.equals(space.getId())) {
                        spaces.add(space);
                        break;
                    }
                }
            }
            return spaces;
        }

        private List<Object> loadEventSpace(List<Space> allSpaces, List<EventType> allEvents, Index index) {
            ArrayList<String> spaceData = index.getSpaceData();
            ArrayList<String> eventData = index.getData();
            ArrayList<Object> eventSpaces = new ArrayList<>();
            for (int i = 0; i < spaceData.size() && i < eventData.size(); i++) {
                String spaceID = spaceData.get(i);
                String eventStaticName = eventData.get(i);
                EventSpaceTrending eventSpace = new EventSpaceTrending();

                for (EventType event :
                        allEvents) {
                    if (event != null && event.getStaticName().equals(eventStaticName)) {
                        eventSpace.setEvent(event);
                        break;
                    }
                }
                if (eventSpace.getEvent() == null) break;

                for (Space space :
                        allSpaces) {
                    if (spaceID != null && spaceID.equals(space.getId())) {
                        eventSpace.setSpace(space);
                        break;
                    }
                }

                if (eventSpace.getSpace() != null) eventSpaces.add(eventSpace);

            }
            return eventSpaces;
        }
    }

    private void setData(List<List<Object>> objectList) {
        mAdapter.setData(mIndexList,objectList);
        mSwipeRefresh.setRefreshing(false);
    }
}
