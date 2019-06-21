package com.spacer.event.ui.main.page.booking;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spacer.event.R;
import com.spacer.event.model.EventType;
import com.spacer.event.model.Space;
import com.spacer.event.ui.main.page.SamplePageThree;
import com.spacer.event.ui.widget.fragmentnavigationcontroller.PresentStyle;
import com.spacer.event.ui.widget.fragmentnavigationcontroller.SupportFragment;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BookingFragment extends SupportFragment {
    private static final String TAG = "BookingFragment";

    public static BookingFragment newInstance(Space space, EventType eventType) {
        BookingFragment fragment = new BookingFragment();
        fragment.mSpace = space;
        fragment.mEventType = eventType;
        return fragment;
    }
    private Space mSpace;
    private EventType mEventType;

    @Nullable
    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.sample_page,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);

    }

    @Override
    public int defaultOpenExitTransition() {
        return PresentStyle.FADE;
    }

    @Override
    public int defaultTransition() {
        return PresentStyle.SLIDE_UP;
    }
}
