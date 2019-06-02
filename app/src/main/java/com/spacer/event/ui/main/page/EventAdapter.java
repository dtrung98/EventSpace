package com.spacer.event.ui.main.page;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.spacer.event.R;
import com.spacer.event.model.EventType;
import com.spacer.event.ui.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ItemHolder> {
    private static final String TAG = "CircleEventTypeAdapter";

    Context mContext;
    private boolean mAdminMode = false;
    private ArrayList<EventType> mData = new ArrayList<>();

    public List<EventType> getData() {
        return mData;
    }
    public ArrayList<Integer> mCountData = new ArrayList<>();

    public interface EventTypeListener {
        void onItemClick(EventType eventType);
    }
    private EventTypeListener mListener;
    public void setListener(EventTypeListener listener) {
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

    public EventAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<EventType> data) {
        mData.clear();
        if (data !=null) {
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void setCountData(List<Integer> data) {
        mCountData.clear();
        if(data!=null) {
            mCountData.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void setData(List<EventType> data,boolean notify) {
        mData.clear();
        if (data !=null) {
            mData.addAll(data);
        }
        if(notify)
        notifyDataSetChanged();
    }

    public void setCountData(List<Integer> data, boolean notify) {
        mCountData.clear();
        if(data!=null) {
            mCountData.addAll(data);
        }
        if(notify)
            notifyDataSetChanged();
    }

    public void addData(List<EventType> data) {
        if(data!=null) {
            int posBefore = mData.size();
            mData.addAll(data);
            notifyItemRangeInserted(posBefore,data.size());
        }
    }


    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_event_list, parent, false);

        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
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

        @BindView(R.id.description) TextView mDescription;

        public ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @OnClick({R.id.image,R.id.constraint_root})
        void clickPanel() {
            if(mListener!=null) mListener.onItemClick(mData.get(getAdapterPosition()));
            else if(mContext instanceof MainActivity) {
                //((MainActivity) mContext).presentFragment(SpaceDetail.newInstance(
                 //       mData.get(getAdapterPosition()), mData.get(getAdapterPosition()).getName()));
            }
        }

        public void bind(EventType eventType) {
            mTitle.setText(eventType.getName());
            Glide.with(mContext)
                    .load(eventType.getIcon())
                    .into(mImage);

            if(mCountData.size()-1>getAdapterPosition()&&mCountData.get(getAdapterPosition())>0) {
                mDescription.setText(mCountData.get(getAdapterPosition())+" "+ mDescription.getResources().getString(R.string.spaces_lower));
            }
            else mDescription.setText("");

        }
    }
}
