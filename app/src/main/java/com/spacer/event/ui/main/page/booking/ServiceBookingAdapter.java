package com.spacer.event.ui.main.page.booking;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.spacer.event.R;
import com.spacer.event.model.Service;
import com.spacer.event.util.SignInOutStatusChanged;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ServiceBookingAdapter extends RecyclerView.Adapter<ServiceBookingAdapter.ItemHolder> {
    private static final String TAG = "ServiceBookingAdapter";
    private Context mContext;
    private ArrayList<Service> mData = new ArrayList<>();

    ServiceBookingAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<Service> list) {
        mData.clear();
        if(list!=null) mData.addAll(list);
        onPriceChanged();
        notifyDataSetChanged();
    }
    private static final int UN_COUNT = 0;
    private static final  int COUNT = 1;

     interface OnPriceChangedListener {
        void onPriceChanged(int newPrice);
    }
    private OnPriceChangedListener mListener;

    public void setOnPriceChangedListener(OnPriceChangedListener listener) {
        mListener = listener;
    }

    public void removeOnPriceChangedListener(){
        mListener = null;
    }

    public void onPriceChanged() {
        int price = 0;
        for (Service service :
                mData) {
            price +=service.getPrice();
        }
        if(mListener!=null)
        mListener.onPriceChanged(price);
    }

    @Override
    public int getItemViewType(int position) {
        if(mData.get(position).getIsCountService()) return COUNT;
        return UN_COUNT;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            if(viewType==UN_COUNT) return new NotCountingItemHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_service,viewGroup,false));
            else
                return new CountingItemHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_service_counting,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder itemHolder, int position) {
        itemHolder.bind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    public ArrayList<Service> getData() {
        return mData;
    }



    class CountingItemHolder extends ItemHolder {
        @BindView(R.id.price_unit)
        TextView mPriceUnit;

        @BindView(R.id.count) EditText mCount;

        @OnClick(R.id.minus)
        void minus() {
          int count =  mData.get(getAdapterPosition()).getCount();
          if(count<1) count =1;
          if(count>1) mData.get(getAdapterPosition()).setCount(count -1);
          bindPrice(mData.get(getAdapterPosition()));
          onPriceChanged();
        }

        @OnClick(R.id.plus)
        void plus() {
            int count =  mData.get(getAdapterPosition()).getCount();
            mData.get(getAdapterPosition()).setCount(count +1);
            bindPrice(mData.get(getAdapterPosition()));
            onPriceChanged();
        }

        @Override
        public void bindPrice(Service service) {

            mCount.setText(""+service.getCount());
            mPriceUnit.setText(""+service.getPrimaryPrice());
            if(service.isBonusService()) mPrice.setText(R.string.free);
            else {
                int price = service.getPrice();
                mPrice.setText(formatMoney(price));
            }
        }


        public CountingItemHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public static String formatMoney(int number) {
        return new DecimalFormat("###,###,###").format(number) +" đ";
    }

    class NotCountingItemHolder extends ItemHolder {
        @Override
        public void bindPrice(Service service) {
            if(service.isBonusService()) mPrice.setText(R.string.free);
            else mPrice.setText(formatMoney(service.getPrimaryPrice()));
        }

        public NotCountingItemHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    abstract class ItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name_text)
        TextView mName;

        @BindView(R.id.price)
        TextView mPrice;

        @BindView(R.id.bonus_text)
        View mBonusText;

        @BindView(R.id.option)
        TextView mOptions;

        public abstract void bindPrice(Service service);

        public void bind(Service service) {
            mName.setText(service.getDetail());
            mOptions.setText(String.format("%s", service.getName()));

            if(service.isBonusService()) {
                mBonusText.setVisibility(View.VISIBLE);
                itemView.setBackgroundResource(R.drawable.background_item_bonus_service);
            }
            else {
                mBonusText.setVisibility(View.GONE);
                itemView.setBackgroundResource(R.drawable.background_item_choose_movie);
            }
            bindPrice(service);
        }

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
