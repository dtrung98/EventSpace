package com.spacer.event.ui.main.page.booking;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.spacer.event.R;
import com.spacer.event.model.EventOrder;
import com.spacer.event.model.UserInfo;
import com.spacer.event.ui.widget.fragmentnavigationcontroller.PresentStyle;
import com.spacer.event.ui.widget.fragmentnavigationcontroller.SupportFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PurchaseScreen extends SupportFragment {
    public static PurchaseScreen newInstance(EventOrder order, UserInfo userInfo) {
        PurchaseScreen fragment = new PurchaseScreen();
        fragment.mOrder = order;
        fragment.mUserInfo = userInfo;
        return fragment;
    }
    private EventOrder mOrder;
    private UserInfo mUserInfo;

    @BindView(R.id.root) View mRoot;

    @Nullable
    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.purchase_screen,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        setContent();
    }

    private void setContent() {
        if(mOrder==null||mUserInfo==null) return;
        mUser.setText(mUserInfo.getFullName());
        mEvent.setText(mOrder.getTitle());
        mSpace.setText(mOrder.getSpaceName());
        mDateFrom.setText(mOrder.getDateFrom());
        mDateTo.setText(mOrder.getDateTo());
        mPurchaseDate.setText(mOrder.getPurchaseTime());

        mPrice.setText(ServiceBookingAdapter.formatMoney(mOrder.getPrice()));
    }

    @OnClick(R.id.back_button)
    void back() {
        getNavigationController().dismissFragment();
    }

    @BindView(R.id.user)
    TextView mUser;

    @BindView(R.id.event)
    TextView mEvent;

    @BindView(R.id.space)
    TextView mSpace;

    @BindView(R.id.date_from)
    TextView mDateFrom;

    @BindView(R.id.date_to)
    TextView mDateTo;

    @BindView(R.id.purchase_date)
    TextView mPurchaseDate;

    @BindView(R.id.price)
    TextView mPrice;



    @Override
    public void onSetStatusBarMargin(int value) {
        mRoot.setPadding(0,value,0,0);
    }

    @Override
    public int defaultOpenExitTransition() {
        return PresentStyle.FADE;
    }
}
