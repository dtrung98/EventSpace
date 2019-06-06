package com.spacer.event.ui.main.page.eventspace;

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

public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "CircleEventTypeAdapter";

    Context mContext;
    private boolean mAdminMode = false;
    private ArrayList<EventType> mData = new ArrayList<>();

    public List<EventType> getData() {
        return mData;
    }
    private ArrayList<Integer> mCountData = new ArrayList<>();
    private ArrayList<AboutProperty> mAboutProperties = new ArrayList<>();
    private Space mSpace;

    public interface EventTypeListener {
        void onEventItemClick(EventType eventType);
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

    public void setSpace(Space space, boolean notify) {
        mSpace = space;
        mAboutProperties.clear();

        if(space!=null) {
            mAboutProperties.add(new AboutProperty(mContext.getString(R.string.about),space.getDetail()));
            mAboutProperties.add(new AboutProperty(mContext.getString(R.string.phone),space.getPhone()));
            mAboutProperties.add(new AboutProperty(mContext.getString(R.string.address),space.getAddress()));
            mAboutProperties.add(new AboutProperty(mContext.getString(R.string.images),""));

        }

        if(notify) notifyDataSetChanged();
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
    public static final int EVENT_TYPE = 0;
    public static final int ABOUT_PROPERTY = 1;
    public static final int IMAGE = 2;

    @Override
    public int getItemViewType(int position) {
        if(position<mData.size()) return EVENT_TYPE;
        else if(position - mAboutProperties.size() < mData.size()) return ABOUT_PROPERTY;
        else return IMAGE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case EVENT_TYPE: return new EventItemHolder(inflater.inflate(R.layout.item_event_list,parent,false));
            case ABOUT_PROPERTY: return new AboutPropertyItemHolder(inflater.inflate(R.layout.item_about_property,parent,false));
            default:return new ImageItemHolder(inflater.inflate(R.layout.item_image_match_parent,parent,false));
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof EventItemHolder) {
            ((EventItemHolder)holder).bind(mData.get(position));
        } else if(holder instanceof AboutPropertyItemHolder) {
            AboutPropertyItemHolder aboutHolder = (AboutPropertyItemHolder)holder;
            if(mSpace!=null) {
                aboutHolder.bind(mAboutProperties.get(position - mData.size()));
            }
        } else if(holder instanceof ImageItemHolder) {
            if(mSpace!=null)

                // 0 1 2 3 4 5 6
                // 0 1 : event
                // 2 3 4 : property
                // 5 6 image

                // 5 - 2 -3 = 0
                ((ImageItemHolder)holder).bind(mSpace.getImages().get(position-mData.size() -mAboutProperties.size()));
        }
    }

    @Override
    public int getItemCount() {
        if(mSpace==null)
        return mData.size();
        else {
         return  mData.size() + mAboutProperties.size() + mSpace.getImages().size();
        }
    }
    public int getSpanCount(){
        return 6;
    }

    public int getSpanSizeItem(int position) {
        int type = getItemViewType(position);
        if(type ==EVENT_TYPE) return 3;
        else if(type==ABOUT_PROPERTY) return 6;
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

    public class ImageItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image) ImageView mImage;
        public ImageItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void bind(String image) {
            Glide.with(mContext).load(image).into(mImage);
        }
    }

    public class EventItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        ImageView mImage;
        @BindView(R.id.title)
        TextView mTitle;

        @BindView(R.id.description) TextView mDescription;

        public EventItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @OnClick({R.id.constraint_root})
        void clickPanel() {
            if(mListener!=null) mListener.onEventItemClick(mData.get(getAdapterPosition()));
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
                mDescription.setVisibility(View.VISIBLE);
                mDescription.setText(mCountData.get(getAdapterPosition())+" "+ mDescription.getResources().getString(R.string.spaces_lower));
            }
            else if(mCountData.isEmpty()) mDescription.setVisibility(View.GONE);
            else {
                mDescription.setVisibility(View.INVISIBLE);
            }

        }
    }
}
