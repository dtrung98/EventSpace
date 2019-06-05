package com.spacer.event.ui.main.page.event;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.spacer.event.R;
import com.spacer.event.model.AboutProperty;
import com.spacer.event.model.EventType;
import com.spacer.event.model.Space;
import com.spacer.event.ui.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SpaceInEventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "SpaceInEventAdapter";


    Context mContext;
    private boolean mAdminMode = false;
    private ArrayList<Space> mData = new ArrayList<>();

    public List<Space> getData() {
        return mData;
    }
    private ArrayList<AboutProperty> mAboutProperties = new ArrayList<>();
    private EventType mEventType;

    public interface SpaceListener {
        void onSpaceItemClick(Space space);
    }

    private SpaceListener mListener;
    public void setListener(SpaceListener listener) {
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

    public SpaceInEventAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<Space> data) {
        mData.clear();
        if (data !=null) {
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }


    public void setEventType(EventType eventType, boolean notify) {
        mEventType = eventType;
        mAboutProperties.clear();

        if(eventType!=null) {
            mAboutProperties.add(new AboutProperty(mContext.getString(R.string.detail),eventType.getDetail()));
            mAboutProperties.add(new AboutProperty(mContext.getString(R.string.services),""));
        }

        if(notify) notifyDataSetChanged();
    }

    public void setData(List<Space> data,boolean notify) {
        mData.clear();
        if (data !=null) {
            mData.addAll(data);
        }
        if(notify)
        notifyDataSetChanged();
    }

    public static final int SPACE_TYPE = 0;
    public static final int ABOUT_PROPERTY = 1;
    public static final int SERVICE = 2;

    @Override
    public int getItemViewType(int position) {
        if(position<mData.size()) return SPACE_TYPE;
        else if(position - mAboutProperties.size() < mData.size()) return ABOUT_PROPERTY;
        else return SERVICE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case SPACE_TYPE: return new SpaceItemHolder(inflater.inflate(R.layout.item_space_of_event,parent,false));
            case ABOUT_PROPERTY: return new AboutPropertyItemHolder(inflater.inflate(R.layout.item_about_property,parent,false));
            default:return new ServiceItemHolder(inflater.inflate(R.layout.item_service_of_event,parent,false));
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof SpaceItemHolder) {
            ((SpaceItemHolder)holder).bind(mData.get(position));
        } else if(holder instanceof AboutPropertyItemHolder) {
            AboutPropertyItemHolder aboutHolder = (AboutPropertyItemHolder)holder;
            if(mEventType!=null) {
                aboutHolder.bind(mAboutProperties.get(position - mData.size()));
            }
        } else if(holder instanceof ServiceItemHolder) {
            if(mEventType!=null)

                // 0 1 2 3 4 5 6
                // 0 1 : space
                // 2 3 4 : property
                // 5 6 service

                // 5 - 2 -3 = 0
                ((ServiceItemHolder)holder).bind(mEventType.getServices().get(position-mData.size() -mAboutProperties.size()));
        }
    }

    @Override
    public int getItemCount() {
        if(mEventType==null)
        return mData.size();
        else {
         return  mData.size() + mAboutProperties.size() + mEventType.getServices().size();
        }
    }
    public int getSpanCount(){
        return 2;
    }

    public int getSpanSizeItem(int position) {
        int type = getItemViewType(position);
        if(type ==SPACE_TYPE) return 1;
        else if(type==ABOUT_PROPERTY) return 2;
        else return 2;
    }
    public class AboutPropertyItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.property) TextView mProperty;
        @BindView(R.id.value) TextView mValue;

        public AboutPropertyItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
        public void bind(AboutProperty property) {
            mProperty.setText(property.getName());
            if(property.getValue().isEmpty())
                mValue.setVisibility(View.GONE);
            else {
                mValue.setText(property.getValue());
                mValue.setVisibility(View.VISIBLE);
            }
        }
    }

    public class ServiceItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.property) TextView mProperty;
        @BindView(R.id.value) TextView mValue;
        public ServiceItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void bind(String service) {
            mProperty.setText(service);
        }
    }

    public class SpaceItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image)
        ImageView mImage;

        @BindView(R.id.title)
        TextView mTitle;

        @BindView(R.id.description)
        TextView mDescription;

        public SpaceItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @OnClick({R.id.panel})
        void clickPanel() {
            if(mListener!=null) mListener.onSpaceItemClick(mData.get(getAdapterPosition()));
            else if(mContext instanceof MainActivity) {
                //((MainActivity) mContext).presentFragment(SpaceDetail.newInstance(
                 //       mData.get(getAdapterPosition()), mData.get(getAdapterPosition()).getName()));
            }
        }

        public void bind(Space space) {
            mTitle.setText(space.getName());

            if(!space.getImages().isEmpty())
            Glide.with(mContext)
                    .load(space.getImages().get(0))
            .placeholder(R.drawable.space_place_holder)
            .error(R.drawable.space_place_holder)
                    .into(mImage);
            mDescription.setText(space.getAddress());
        }
    }
}
