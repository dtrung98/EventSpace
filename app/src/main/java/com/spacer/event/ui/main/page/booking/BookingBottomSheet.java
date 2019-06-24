package com.spacer.event.ui.main.page.booking;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.spacer.event.R;
import com.spacer.event.model.EventType;
import com.spacer.event.model.Space;
import com.spacer.event.ui.widget.fragmentnavigationcontroller.PresentStyle;
import com.spacer.event.ui.widget.fragmentnavigationcontroller.SupportFragment;
import com.spacer.event.util.Tool;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.support.design.widget.BottomSheetBehavior.STATE_COLLAPSED;

public class BookingBottomSheet extends BottomSheetDialogFragment {
    private static final String TAG = "BookingFragment";

    public static BookingBottomSheet newInstance(Space space, EventType eventType) {
        BookingBottomSheet fragment = new BookingBottomSheet();
        fragment.mSpace = space;
        fragment.mEventType = eventType;
        return fragment;
    }

    @OnClick(R.id.back)
    void back() {
        dismiss();
    }

    @BindView(R.id.root) View mRoot;
    @BindView(R.id.scroll_view) View mScrollView;
    private Space mSpace;
    private EventType mEventType;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.booking,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
                FrameLayout bottomSheet = (FrameLayout)
                        dialog.findViewById(android.support.design.R.id.design_bottom_sheet);
                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setPeekHeight(-Tool.getNavigationHeight(requireActivity()));
                behavior.setHideable(false);
                behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {
                        if (newState == STATE_COLLAPSED)
                            BookingBottomSheet.this.dismiss();
                    }

                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                    }
                });
            }
        });
        onViewCreated(view);
    }
    private void onViewCreated(View view) {
        ButterKnife.bind(this,view);
        mScrollView.getLayoutParams().height = (int) (Tool.getScreenSize(getContext())[1] - Tool.getStatusHeight(getResources()) - getResources().getDimension(R.dimen.dp50));
        mScrollView.requestLayout();
    }
}
