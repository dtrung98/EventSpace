package com.spacer.event.ui.main.root.spacetab;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nex3z.flowlayout.FlowLayout;
import com.spacer.event.R;
import com.spacer.event.model.Space;
import com.spacer.event.ui.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SpaceAdapter extends RecyclerView.Adapter<SpaceAdapter.ItemHolder> {
    private static final String TAG = "SpaceAdapter";
    Context mContext;
    private boolean mAdminMode = false;
    private ArrayList<Space> mData = new ArrayList<>();

    public List<Space> getData() {
        return mData;
    }

    public interface SpaceListener {
        void onItemClick(Space space);
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

    public SpaceAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<Space> data) {
        mData.clear();
        if (data !=null) {
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void addData(List<Space> data) {
        if(data!=null) {
            int posBefore = mData.size();
            mData.addAll(data);
            notifyItemRangeInserted(posBefore,data.size());
        }
    }


    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_space_tab, parent, false);

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

        @BindView(R.id.detail)
        TextView mDetail;

        @BindView(R.id.event_flow_layout)
        FlowLayout mEventFlowLayout;



        public ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @OnClick(R.id.touch_panel)
        void clickPanel() {
            if(mListener!=null) mListener.onItemClick(mData.get(getAdapterPosition()));
            else if(mContext instanceof MainActivity) {
                //((MainActivity) mContext).presentFragment(SpaceDetail.newInstance(
                 //       mData.get(getAdapterPosition()), mData.get(getAdapterPosition()).getName()));
            }
        }

        public void bind(Space space) {




            String Address = space.getAddress();

//            if (Address.length() >= 78) // Nhieu hon 78 ky tu thi nhung ky tu sau phai ghi bang ...
//            {
//                Address = Address.substring(0, Math.min(Address.length(), 78));
//                Address += "...";
//            }

            mTitle.setText(space.getName());
            //  mDetail.setText(space.getSupportEvents().size()+" "+mDetail.getContext().getString(R.string.events)+"\n" + mDetail.getContext().getString(R.string.middle_dot) +"\n"+space.getDetail());
           // mDetail.setText(space.getSupportEvents().size()+" "+mDetail.getContext().getString(R.string.events));
              mDetail.setText(space.getSupportEvents().size()+" "+mDetail.getContext().getString(R.string.events)+" " + mDetail.getContext().getString(R.string.middle_dot) +" "+space.getAddress());

            if(!space.getImages().isEmpty())
            Glide.with(mContext)
                    .load(space.getImages().get(0))
                    .into(mImage);
             mEventFlowLayout.removeAllViews();

             if(mContext instanceof Activity) {
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();

                for (int i = 0; i < 4 &&i < space.getEventTypeNames().size(); i++) {
                    TextView evenText = (TextView) inflater.inflate(R.layout.item_eventspace_text_view,mEventFlowLayout,false);
                    evenText.setText(space.getEventTypeNames().get(i));
                    mEventFlowLayout.addView(evenText);
                }
            }

        }
    }
}
