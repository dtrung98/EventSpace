package com.spacer.event.ui.main.root.spacetab;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.spacer.event.model.EventType;
import com.spacer.event.model.Space;
import com.spacer.event.ui.main.MainActivity;
import com.spacer.event.ui.main.page.SearchPage;
import com.spacer.event.util.Tool;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SpaceTab extends Fragment {
    private static final String TAG = "SpaceTab";

    @BindView(R.id.root) View mRoot;
    
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    @BindView(R.id.space_recycle_view)
    RecyclerView mSpaceRecyclerView;

    @BindView(R.id.filter_recycler_view)
    RecyclerView mFilterRecyclerView;

    @BindView(R.id.filter_panel)
            View mFilterPanel;

    SpaceAdapter mSpaceAdapter;
    CircleEventTypeAdapter mEventTypeAdapter;

    @OnClick(R.id.search_panel)
    void goToSearchPage() {
        if(getActivity() instanceof MainActivity)
        ((MainActivity)getActivity()).presentFragment(SearchPage.newInstance());
    }

    @OnClick(R.id.see_all_panel)
    void seeAllEvents() {

    }

    public static SpaceTab newInstance() {
        return new SpaceTab();
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
        mSwipeRefresh.setOnRefreshListener(this::refreshData);
        mSwipeRefresh.setColorSchemeResources(R.color.FlatOrange);
        mSwipeRefresh.setProgressViewOffset(true,mSwipeRefresh.getProgressViewStartOffset(),mSwipeRefresh.getProgressViewEndOffset());
      //  mSwipeRefresh.setProgressBackgroundColorSchemeResource(R.color.FocusYellowColor);
        mSwipeRefresh.setRefreshing(true);
        refreshData();
    }

    public void init() {
        mRoot.setPadding(0,Tool.getStatusHeight(getResources()),0,0);
        mSpaceAdapter = new SpaceAdapter(getActivity());
        mSpaceRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        mSpaceRecyclerView.setAdapter(mSpaceAdapter);

        mEventTypeAdapter = new CircleEventTypeAdapter(getActivity());
        mFilterRecyclerView.setAdapter(mEventTypeAdapter);
        mFilterRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
    }

    private void refreshData() {
        FirebaseFirestore.getInstance()
                .collection("spaces")
                .get()
                .addOnSuccessListener(mSpaceListener)
                .addOnFailureListener(mSpaceListener);

        FirebaseFirestore.getInstance()
                .collection("event_types")
                .get()
                .addOnSuccessListener(mEventTypeListener)
                .addOnFailureListener(mEventTypeListener);
    }

    private FireBaseCollectionListener mSpaceListener = new FireBaseCollectionListener() {
        @Override
        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
            mSwipeRefresh.setRefreshing(false);
            ArrayList<Space> list = (ArrayList<Space>) queryDocumentSnapshots.toObjects(Space.class);
            mSpaceAdapter.setData(list);
            Log.d(TAG, "onSuccess: "+list);

        /*    for (int i = 1; i < 5; i++) {
                FirebaseFirestore.getInstance()
                        .collection("spaces")
                        .document(""+i)
                        .set(list.get(0));
            }*/


        }

        @Override
        public void onFailure(@NonNull Exception e) {
            mSwipeRefresh.setRefreshing(false);
            Log.d(TAG, "onFailure");
        }
    };
    private FireBaseCollectionListener mEventTypeListener = new FireBaseCollectionListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            mFilterPanel.setVisibility(View.GONE);
        }

        @Override
        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
            ArrayList<EventType> eventTypes = (ArrayList<EventType>) queryDocumentSnapshots.toObjects(EventType.class);
            Log.d(TAG, "onSuccess: getting eventTypes");
            mEventTypeAdapter.setData(eventTypes);
            mFilterPanel.setVisibility(View.VISIBLE);
          /*
           for (int i = 1; i < 6; i++) {
                FirebaseFirestore.getInstance()
                        .collection("event_types")
                        .document(""+i)
                        .set(eventTypes.get(0));
            }
            */
        }
    };

}
