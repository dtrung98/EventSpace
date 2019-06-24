package com.spacer.event.ui.main.root.trending;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.spacer.event.R;
import com.spacer.event.model.AboutProperty;
import com.spacer.event.model.EventSpace;
import com.spacer.event.model.EventSpaceTrending;
import com.spacer.event.model.EventType;
import com.spacer.event.model.Index;
import com.spacer.event.model.Space;
import com.spacer.event.ui.main.MainActivity;
import com.spacer.event.ui.main.page.eventspace.SpaceDetailFragment;
import com.spacer.event.ui.main.page.search.SearchAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ItemTrendingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "CircleEventTypeAdapter";

    Context mContext;
    private boolean mAdminMode = false;
    private ArrayList<Object> mData = new ArrayList<>();
    private ArrayList<Integer> mCountData = new ArrayList<>();

    public List<Object> getData() {
        return mData;
    }

    private SearchAdapter.TypeListener mListener;
    public void setListener(SearchAdapter.TypeListener listener) {
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

    public ItemTrendingAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<Object> data) {
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

    public final static int EVENT = 0;
    public final static int SPACE = 1;
    public final static int EVENT_SPACE = 2;

    @Override
    public int getItemViewType(int position) {
        if(mData.get(position) instanceof EventType) return EVENT;
        if(mData.get(position) instanceof Space) return SPACE;
        if(mData.get(position) instanceof EventSpaceTrending) return EVENT_SPACE;
        return EVENT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case EVENT: return new EventItemHolder(inflater.inflate(R.layout.item_event_list,parent,false));
            case SPACE: return new SpaceItemHolder(inflater.inflate(R.layout.item_space_of_event,parent,false));
            default:return new EventSpaceItemHolder(inflater.inflate(R.layout.item_event_space_trending,parent,false));
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof EventItemHolder) {
            ((EventItemHolder)holder).bind((EventType) mData.get(position));
        } else if(holder instanceof SpaceItemHolder) {
            ((SpaceItemHolder)holder).bind((Space)mData.get(position));
        } else if(holder instanceof EventSpaceItemHolder) {
            ((EventSpaceItemHolder)holder).bind((EventSpaceTrending)mData.get(position));
        }
    }

    @Override
    public int getItemCount() {
       return mData.size();
    }
    public int getSpanCount(){
        return 6;
    }

    public RecyclerView.LayoutManager getLayoutManager(Context context, String type) {
            //if(Index.EVENT.equals(type))
                return new GridLayoutManager(context,2,LinearLayoutManager.VERTICAL,false);
            //else return new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
    }

/*    public int getSpanSizeItem(int position) {
        int type = getItemViewType(position);
        if(type ==EVENT_TYPE) return 3;
        else if(type==ABOUT_PROPERTY) return 6;
        else return 2;
    }*/


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
            if(mListener!=null) mListener.onTypeItemClick(mData.get(getAdapterPosition()));
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
            if(mListener!=null) mListener.onTypeItemClick(mData.get(getAdapterPosition()));
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

    public class EventSpaceItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.event_image)
        ImageView mEventImage;

        @BindView(R.id.space_image)
        ImageView mSpaceImage;

        @BindView(R.id.title)
        TextView mTitle;

        @BindView(R.id.description)
        TextView mDescription;

        public EventSpaceItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @OnClick({R.id.panel})
        void clickPanel() {
            if(mListener!=null) mListener.onTypeItemClick(mData.get(getAdapterPosition()));
            else if(mContext instanceof MainActivity) {
                //((MainActivity) mContext).presentFragment(SpaceDetail.newInstance(
                //       mData.get(getAdapterPosition()), mData.get(getAdapterPosition()).getName()));
            }
        }

        public void bind(EventSpaceTrending eventspace) {
            if(eventspace.getSpace()!=null) {
                if (eventspace.getSpace().getName() != null)
                    mTitle.setText(eventspace.getSpace().getName());
                if (!eventspace.getSpace().getImages().isEmpty())
                    Glide.with(mContext)
                            .load(eventspace.getSpace().getImages().get(0))
                            .placeholder(R.drawable.space_place_holder)
                            .error(R.drawable.space_place_holder)
                            .into(mSpaceImage);

            }

            if(eventspace.getEvent()!=null) {
                mDescription.setText(eventspace.getEvent().getName());
                Glide.with(mContext)
                        .load(eventspace.getEvent().getIcon())
                        .placeholder(R.drawable.event)
                        .error(R.drawable.event)
                        .into(mEventImage);
            }

        }
    }
}
