package com.spacer.event.ui.main.page.booking;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.spacer.event.R;
import com.spacer.event.listener.FireBaseCollectionListener;
import com.spacer.event.listener.FireBaseGetDocumentResultListener;
import com.spacer.event.model.EventOrder;
import com.spacer.event.model.EventType;
import com.spacer.event.model.Service;
import com.spacer.event.model.Space;
import com.spacer.event.model.UserInfo;
import com.spacer.event.ui.main.MainActivity;
import com.spacer.event.ui.main.page.eventspace.SpaceDetailFragment;
import com.spacer.event.ui.main.page.inout.SignInFragment;
import com.spacer.event.ui.widget.SuccessTickView;
import com.spacer.event.ui.widget.fragmentnavigationcontroller.PresentStyle;
import com.spacer.event.ui.widget.fragmentnavigationcontroller.SupportFragment;
import com.spacer.event.util.SignInOutStatusChanged;
import com.tuyenmonkey.mkloader.MKLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class BookingFragment extends SupportFragment implements SignInOutStatusChanged, BookingAdapter.OnPriceChangedListener {
    private static final String TAG = "BookingFragment";

    public static BookingFragment newInstance(Space space, EventType eventType) {
        BookingFragment fragment = new BookingFragment();
        fragment.mSpace = space;
        fragment.mEventType = eventType;
        return fragment;
    }

    @BindView(R.id.root)
    View mRoot;

    @OnClick(R.id.back)
    void back() {
        if(!mAdapter.getData().isEmpty()) {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
            bottomSheetDialog.setContentView(R.layout.bottom_alert);
            bottomSheetDialog.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialog.dismiss();
                    getNavigationController().dismissFragment();
                }
            });
            bottomSheetDialog.show();
        } else {
            getNavigationController().dismissFragment();
        }
    }

    @Override
    public boolean onBackPressed() {
        back();
        return false;
    }

    @BindView(R.id.space_image)
    ImageView mSpaceImage;

    @BindView(R.id.image)
    ImageView mEventImage;

    @BindView(R.id.space_name)
    TextView mSpaceName;

    @BindView(R.id.event_name)
    TextView mEventName;

    @BindView(R.id.date_from)
    TextView mDateFrom;

    @BindView(R.id.date_to)
    TextView mDateTo;

    @BindView(R.id.continue_icon)
    ImageView mContinueIcon;

    @BindView(R.id.loader)
    View mLoader;

    @BindView(R.id.recycler_view)
    RecyclerView mRecylerView;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    @BindView(R.id.option_panel) View mOptionPanel;

    @OnClick(R.id.date_from)
    void setDateFrom() {
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        String str_day = Integer.toString(day);
                        String str_month = Integer.toString(month + 1);
                        if (1 == str_day.length()) {
                            str_day = "0" + str_day;
                        }
                        if (1 == str_month.length()) {
                            str_month = "0" + str_month;
                        }
                        mOrder.setDateFrom(String.format("%s/%s/%d", str_day, str_month, year));
                        mDateFrom.setText(mOrder.getDateFrom());
                        checkoutAbleToRefreshing();
                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    @OnClick(R.id.date_to)
    void setDateTo() {
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        String str_day = Integer.toString(day);
                        String str_month = Integer.toString(month + 1);
                        if (1 == str_day.length()) {
                            str_day = "0" + str_day;
                        }
                        if (1 == str_month.length()) {
                            str_month = "0" + str_month;
                        }
                        mOrder.setDateTo(String.format("%s/%s/%d", str_day, str_month, year));
                        mDateTo.setText(mOrder.getDateTo());
                        checkoutAbleToRefreshing();
                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }
    EventOrder mOrder = new EventOrder();

    /*@OnTextChanged({R.id.date_from,R.id.date_to})
    void onNameChanged(CharSequence s, int start, int before, int count) {
        checkoutAbleToRefreshing();
    }*/

    void checkoutAbleToRefreshing() {
        mLoader.setVisibility(View.GONE);
        mContinueIcon.setVisibility(View.VISIBLE);
        if(mOrder.getDateTo().isEmpty()||mOrder.getDateFrom().isEmpty()) {
            mContinueIcon.setEnabled(false);
            mContinueIcon.setColorFilter(0xff555555);
            mContinueIcon.setBackgroundResource(R.drawable.background_continue_disable);
        } else {
            mLoader.setVisibility(View.GONE);
            mContinueIcon.setColorFilter(getResources().getColor(R.color.FlatOrange));
            mContinueIcon.setBackgroundResource(R.drawable.background_continue);
            mContinueIcon.setEnabled(true);
        }
    }

    @OnClick(R.id.info_button)
    void goToDetailSpace() {
        getNavigationController().presentFragment(SpaceDetailFragment.newInstance(null,null,mSpace));
    }

    @OnClick(R.id.continue_icon)
    void continueClick() {

        if(mLoader.getVisibility()==View.GONE) {
            mContinueIcon.setVisibility(View.INVISIBLE);
            mLoader.setVisibility(View.VISIBLE);
            refreshData();
        }
    }

    private Space mSpace;
    private EventType mEventType;

    @Nullable
    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.booking,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);

        mAdapter = new BookingAdapter(getContext());
        mAdapter.setOnPriceChangedListener(this);

        mRecylerView.setAdapter(mAdapter);
        mRecylerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        mSwipeRefresh.setColorSchemeResources(R.color.FlatOrange);
        mSwipeRefresh.setProgressViewOffset(true,mSwipeRefresh.getProgressViewStartOffset()+(int)getResources().getDimension(R.dimen.swipe_top_off_set),mSwipeRefresh.getProgressViewEndOffset()+(int)getResources().getDimension(R.dimen.swipe_top_off_set));
        mSwipeRefresh.setOnRefreshListener(this::refreshData);

        if(getActivity() instanceof MainActivity) {
            ((MainActivity)getActivity()).addSignInOutStatusListener(this);
        }
        bind();
    }

    @Override
    public void onDestroyView() {
        if(getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).removeSignInOutListener(this);
        }
        super.onDestroyView();
    }
    public int[] mRefreshResult = new int[] {0,0};

    private static int UN_SET = 0;
    private static int SUCCESS = 1;
    private static int FAILURE = 2;
    private FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }
    private UserInfo getCurrentUserInfo() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null&&mPrivateUserInfo!=null&&user.getUid().equals(mPrivateUserInfo.getId())) return mPrivateUserInfo;
        return null;
    }

    private void refreshData() {
        mRefreshResult[0] = UN_SET;
        mRefreshResult[1] = UN_SET;

        if (mOrder.getDateTo().isEmpty() || mOrder.getDateFrom().isEmpty()) {
            mSwipeRefresh.setRefreshing(false);
        } else {
            mSwipeRefresh.setRefreshing(true);
            FirebaseFirestore.getInstance()
                    .collection("services")
                    .get()
                    .addOnFailureListener(mServiceListener)
                    .addOnSuccessListener(mServiceListener);
            if(getCurrentUser()!=null)
                FirebaseFirestore.getInstance()
                .collection("user_infos")
                        .document(getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(mUserInforListener)
                .addOnFailureListener(mUserInforListener);
            else mRefreshResult[1]=SUCCESS;
        }
    }

    @SuppressLint({"DefaultLocale", "SimpleDateFormat"})
    private String getCurrentDate(boolean time) {



        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat sdf;
        if(time)
       sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        else sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(calendar.getTime());

        /*String str_day = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        String str_month = Integer.toString(calendar.get(Calendar.MONTH) + 1);
        if (1 == str_day.length()) {
            str_day = "0" + str_day;
        }
        if (1 == str_month.length()) {
            str_month = "0" + str_month;
        }
        if(time) return String.format("%s %s/%s/%d",calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.Mi) str_day,str_month,calendar.get(Calendar.YEAR));
        return String.format("%s/%s/%d", str_day,str_month,calendar.get(Calendar.YEAR));*/
    }

    @SuppressLint("DefaultLocale")
    private void bind() {

        mOrder.setDateFrom(getCurrentDate(false));
        mDateFrom.setText(mOrder.getDateFrom());
        checkoutAbleToRefreshing();

        if(mSpace!=null) {
            if(!mSpace.getImages().isEmpty())
            Glide.with(this)
                    .load(mSpace.getImages().get(0))
                    .into(mSpaceImage);

            Glide.with(this)
                    .load(mEventType.getIcon())
                    .placeholder(R.drawable.event)
                    .error(R.drawable.event)
                    .into(mEventImage);

            mSpaceName.setText(mSpace.getName());
            mEventName.setText(mEventType.getName());
        }
    }

    @Override
    public int defaultOpenExitTransition() {
        return PresentStyle.FADE;
    }

    @Override
    public int defaultDuration() {
        return 225;
    }

    @Override
    public int defaultTransition() {
        return PresentStyle.SLIDE_UP;
    }

    @Override
    public void onSetStatusBarMargin(int value) {
        mRoot.setPadding(0,value,0,0);
    }
    private void checkResult() {
        if(mRefreshResult[0]==UN_SET||mRefreshResult[1]==UN_SET) return;
        mSwipeRefresh.setRefreshing(false);
        mLoader.setVisibility(View.GONE);

        if(mRefreshResult[0]==FAILURE||mRefreshResult[1]==FAILURE) {
            mContinueIcon.setVisibility(View.VISIBLE);
            mOptionPanel.setVisibility(View.VISIBLE);
            mServiceTitle.setVisibility(View.GONE);
            mPurchasePanel.setVisibility(View.GONE);
            mDateFrom.setEnabled(true);
            mDateTo.setEnabled(true);
        } else {
            mDateFrom.setEnabled(false);
            mDateTo.setEnabled(false);
            mContinueIcon.setVisibility(View.GONE);
            mOptionPanel.setVisibility(View.GONE);
            mServiceTitle.setVisibility(View.VISIBLE);
            mPurchasePanel.setVisibility(View.VISIBLE);
            showService();
        }
    }

    @BindView(R.id.purchase_panel) View mPurchasePanel;
    @BindView(R.id.purchase_price) TextView mPurchasePrice;
    @BindView(R.id.purchase_text) TextView mPurchaseText;

    private final static int NOT_SIGNED_IN = 1;
    private final static int NO_USER_INFO = 2;
    private final static int NOT_ENOUGH_MONEY = 3;
    private final static int PURCHASE_OK = 4;

    private int mPurchaseStatus = UN_SET;

    private void bindPurchaseButton() {
        if(mPurchaseStatus==UN_SET) checkPurchaseStatus();

        if(mPurchaseStatus==NOT_SIGNED_IN) {
            mPurchasePrice.setVisibility(View.GONE);
            mPurchaseText.setText(R.string.plz_sign_in);
            mPurchasePanel.setBackgroundResource(R.drawable.background_sign_in);
            mPurchasePanel.setEnabled(true);
        } else if(mPurchaseStatus==NO_USER_INFO) {
           mPurchasePrice.setVisibility(View.GONE);
           mPurchaseText.setText(R.string.plz_refresh);
           mPurchasePanel.setBackgroundResource(R.drawable.background_warn);
           mPurchasePanel.setEnabled(true);
        }
        else if(mPurchaseStatus==NOT_ENOUGH_MONEY) {
            mPurchasePrice.setVisibility(View.VISIBLE);
            mPurchasePrice.setText("Total Price : "+ BookingAdapter.formatMoney(mPrice));
            mPurchaseText.setText(R.string.not_enough_money);
            mPurchasePanel.setEnabled(false);
            mPurchasePanel.setBackgroundResource(R.drawable.background_purchase_disable);
        } else {
            mPurchasePrice.setVisibility(View.VISIBLE);
            mPurchasePrice.setText("Total Price : "+ BookingAdapter.formatMoney(mPrice));
            mPurchaseText.setText(R.string.purchase_now);
            mPurchasePanel.setBackgroundResource(R.drawable.background_purchase);
            mPurchasePanel.setEnabled(true);
        }
    }

    private void checkPurchaseStatus() {

        if(getCurrentUser()==null) mPurchaseStatus = NOT_SIGNED_IN;
        else if(getCurrentUserInfo()==null) mPurchaseStatus = NO_USER_INFO;
        else if(getCurrentUserInfo().getBalance()<mPrice) mPurchaseStatus = NOT_ENOUGH_MONEY;
        else mPurchaseStatus = PURCHASE_OK;
        bindPurchaseButton();
    }

    @OnClick(R.id.purchase_panel)
    void tryToPurchase() {
        if(mPurchaseStatus==UN_SET) checkPurchaseStatus();

        switch (mPurchaseStatus) {
            case NOT_SIGNED_IN: getNavigationController().presentFragment(SignInFragment.newInstance()); break;
            case NO_USER_INFO:refreshData();
            case NOT_ENOUGH_MONEY:break;
            case PURCHASE_OK: purchaseNow();
        }
    }

    void purchaseNow() {
        if(getCurrentUser()==null||getCurrentUserInfo()==null) return;

        mOrder.setUserUID(getCurrentUserInfo().getId());
        mOrder.setEvent(mEventType.getStaticName());
        mOrder.setTitle(mEventType.getName());
        mOrder.setPrice(mPrice);
        mOrder.setSpaceID(mSpace.getId());
        mOrder.setSpaceName(mSpace.getName());
        mOrder.setServices(mAdapter.getData());
        Calendar calendar = Calendar.getInstance();
        mOrder.setPurchaseTime(getCurrentDate(true));

        mPrivateUserInfo.setBalance(mPrivateUserInfo.getBalance() - mPrice);
        int i = 1;
        do {
            mOrder.setId(getCurrentUserInfo().getId()+ "_" + mEventType.getStaticName() + "_" + mSpace.getId() + "_" + i);
            i++;
        } while (mPrivateUserInfo.getOrderIDs().contains(mOrder.getId()));
        mPrivateUserInfo.getOrderIDs().add(mOrder.getId());

        mSendingDialog = new BottomSheetDialog(mSwipeRefresh.getContext());

        mSendingDialog.setContentView(R.layout.sending_data);
        mSendingDialog.setCancelable(false);
        View closeButton = mSendingDialog.findViewById(R.id.close);
        if(closeButton!=null)
        closeButton.setOnClickListener(v -> cancelSending());
        mSendingDialog.show();

        FirebaseFirestore.getInstance()
                .collection("event_orders")
                .document(mOrder.getId())
                .set(mOrder)
                .addOnSuccessListener(aVoid -> {
                FirebaseFirestore.getInstance()
                        .collection("user_infos")
                        .document(mPrivateUserInfo.getId())
                        .update("balance",mPrivateUserInfo.getBalance(),"orderIDs",mPrivateUserInfo.getOrderIDs())
                        .addOnSuccessListener(aVoid1 -> {
                            setOnSuccess();
                        })
                        .addOnFailureListener(e -> {
                           setOnFailure();
                        });
                })
                .addOnFailureListener(e -> {
                    setOnFailure();
                });
    }


    private BottomSheetDialog mSendingDialog;
    boolean cancelled = false;
    void cancelSending() {
        if(mSendingDialog!=null)
            mSendingDialog.dismiss();
        cancelled = true;
    }

    void setTextSending(String text,int color) {
        if(mSendingDialog!=null) {
            TextView textView = mSendingDialog.findViewById(R.id.sending_text);
            if(textView!=null) {

                AlphaAnimation aa = new AlphaAnimation(0,1);
                aa.setFillAfter(true);
                aa.setDuration(500);
                textView.setText(text);
                textView.setTextColor(color);
                textView.startAnimation(aa);
            }
        }
    }
    void setOnSuccess() {
        if(cancelled) return;
        cancelled= false;
        if(mSendingDialog!=null) {
            MKLoader mkLoader = mSendingDialog.findViewById(R.id.loading);
            if(mkLoader!=null) mkLoader.setVisibility(View.INVISIBLE);
            SuccessTickView s = mSendingDialog.findViewById(R.id.success_tick_view);
            if(s!=null) {

                s.postDelayed(() -> {
                    mSendingDialog.dismiss();
                    mSwipeRefresh.postDelayed(this::showTicketPrint,350);
                },2000);
                s.setVisibility(View.VISIBLE);
                s.startTickAnim();
                setTextSending("The orders is successfully created !",getResources().getColor(R.color.FlatGreen));
            }
        }
    }
    void setOnFailure() {
        if(mSendingDialog!=null){
            mSendingDialog.dismiss();
            if(getContext()!=null)
            Toasty.error(getContext(),"Cannot finish your purchase, please try again!").show();
        }
    }

    void showTicketPrint() {
        getNavigationController().dismissFragment();
        getNavigationController().presentFragment(PurchaseScreen.newInstance(mOrder,mPrivateUserInfo));
    }

    private ArrayList<Service> mAllServices = new ArrayList<>();
    private BookingAdapter mAdapter;
    private void showService() {
        ArrayList<Service> list = new ArrayList<>();
        list.clear();
        for (String serviceName :
                mEventType.getServices()) {
            for (Service service :
                    mAllServices) {
                if(serviceName.equals(service.getStaticName())) list.add(service);
            }
        }

        Collections.sort(list, new Comparator<Service>() {
            @Override
            public int compare(Service o1, Service o2) {
                if(o1.getIsCountService()&&o2.getIsCountService()) return 0;
                if(o1.getIsCountService()) return -1;
                return 1;
            }
        });


        mAdapter.setData(list);
        mScrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScrollView.smoothScrollTo(0, (int) mServiceTitle.getY());
                checkPurchaseStatus();

            }
        },350);

    }
    @BindView(R.id.service_title) View mServiceTitle;

    @BindView(R.id.scroll_view)
    NestedScrollView mScrollView;

    private FireBaseCollectionListener mServiceListener = new FireBaseCollectionListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            if(getContext()!=null)
            Toasty.error(getContext(),e.getMessage()).show();
            mRefreshResult[0] = FAILURE;
            checkResult();
        }

        @Override
        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
            mAllServices.clear();
            mAllServices.addAll(queryDocumentSnapshots.toObjects(Service.class));
            mRefreshResult[0] = SUCCESS;
            checkResult();
        }
    };

    private FireBaseGetDocumentResultListener mUserInforListener = new FireBaseGetDocumentResultListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            mRefreshResult[1] = FAILURE;
            checkResult();
        }

        @Override
        public void onSuccess(DocumentSnapshot documentSnapshot) {
            if(documentSnapshot.exists()) {
                mRefreshResult[1] = SUCCESS;
                mPrivateUserInfo = documentSnapshot.toObject(UserInfo.class);
            } else {
                mRefreshResult[1] = FAILURE;
                if(getContext()!=null)
                Toasty.error(getContext(),"Sorry, can not get your profile!!").show();
            }
            checkResult();

        }
    };
    private UserInfo mPrivateUserInfo;

    @Override
    public void onJustSignIn(FirebaseUser user) {
        refreshData();
    }

    @Override
    public void onJustSignOut() {
        refreshData();
    }
    private int mPrice = 0;

    @Override
    public void onPriceChanged(int newPrice) {
        mPrice = newPrice;
        checkPurchaseStatus();
    }
}
