package com.spacer.event.ui.main.page.search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.spacer.event.R;

import com.spacer.event.model.EventType;
import com.spacer.event.model.Service;
import com.spacer.event.model.Space;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ItemHolder> {
    private static final String TAG = "CircleEventTypeAdapter";

    Context mContext;
    private boolean mAdminMode = false;
    private ArrayList<Object> mData = new ArrayList<>();

    public List<Object> getData() {
        return mData;
    }

    public interface TypeListener {
        void onTypeItemClick(Object object);
    }
    private TypeListener mListener;
    public void setListener(TypeListener listener) {
        mListener = listener;
    }
    public void removeListener() {
        mListener = null;
    }

    /*
    Turn on this to allow adapter to show option button when swipe right
     */

    public void turnOnAdminMode() {
        mAdminMode = true;
    }

    public SearchAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<Object> data) {

        mData.clear();
        if (data !=null) {
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }


    public void addData(List<Object> data) {
        if(data!=null) {
            int posBefore = mData.size();
            mData.addAll(data);
            notifyItemRangeInserted(posBefore,data.size());
        }
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new ItemHolder(inflater.inflate(R.layout.item_search_fragment,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
      holder.bind(mData.get(position));
    }

    @Override
    public int getItemCount() {
       return mData.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
       ImageView mImage;

        @BindView(R.id.title)
        TextView mTitle;

        @BindView(R.id.description)
        TextView mDescription;
        public ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void bind(Object object) {
            if(object instanceof EventType)
                bindEvent((EventType)object);
            else if (object instanceof Space)
                bindSpace((Space)object);
            else if (object instanceof Service)
                bindService((Service)object);
            else hideItem();
        }

        public void bindEvent(EventType eventType) {
            mTitle.setText(eventType.getName());
            mDescription.setText(mDescription.getResources().getString(R.string.event));
            mImage.setBackgroundResource(R.drawable.background_circle_event_filter);
            int padding = (int) mImage.getResources().getDimension(R.dimen.dp16);
            mImage.setPadding(padding,padding,padding,padding);
            Glide.with(mContext)
                    .load(eventType.getIcon())
                    .placeholder(R.drawable.event)
                    .error(R.drawable.event)
                    .into(mImage);
        }

        public void bindSpace(Space space) {
            mTitle.setText(space.getName());
            mDescription.setText(mDescription.getResources().getString(R.string.space)+" "+mDescription.getResources().getString(R.string.middle_dot)+" " + space.getAddress());

            mImage.setPadding(0,0,0,0);
            mImage.setBackground(null);

            if(!space.getImages().isEmpty())
            Glide.with(mContext)
                    .load(space.getImages().get(0))
            .placeholder(R.drawable.domain)
            .error(R.drawable.domain)
            .into(mImage);
            else mImage.setImageResource(R.drawable.domain);
        }

        public void bindService(Service service) {
            mTitle.setText(service.getName());
            mDescription.setText(mDescription.getResources().getString(R.string.service));
            mImage.setBackground(null);
            mImage.setImageResource(R.drawable.content);
            mImage.setPadding(0,0,0,0);
        }

        public void hideItem() {
            itemView.setVisibility(View.GONE);
        }

        @OnClick(R.id.root)
        void clickPanel() {
            if(mListener!=null) mListener.onTypeItemClick(mData.get(getAdapterPosition()));
        }
    }
}
