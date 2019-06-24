package com.spacer.event.ui.main.root.trending;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.spacer.event.R;
import com.spacer.event.model.Index;
import com.spacer.event.ui.main.page.search.SearchAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrendingAdapter extends RecyclerView.Adapter<TrendingAdapter.TrendingHolder> implements SearchAdapter.TypeListener {
    private static final String TAG = "TrendingAdapter";
    private ArrayList<Index> mIndexData = new ArrayList<>();
    private ArrayList<List<Object>> mObjectData= new ArrayList<>();

    private RecyclerView.RecycledViewPool mViewPool = new RecyclerView.RecycledViewPool();

    public void setData(List<Index> indexList, List<List<Object>> objectList) {
        mIndexData.clear();
        mObjectData.clear();

        if(indexList!=null) mIndexData.addAll(indexList);
        if(objectList!=null) mObjectData.addAll(objectList);

        notifyDataSetChanged();
    }
    private SearchAdapter.TypeListener mListener;
    public void setListener(SearchAdapter.TypeListener listener) {
        mListener = listener;
    }

    public void removeListener() {
        mListener = null;
    }

    TrendingAdapter() {
    }

    @NonNull
    @Override
    public TrendingHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TrendingHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_trending,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TrendingHolder viewHolder, int i) {
        viewHolder.bind(mIndexData.get(i));
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mIndexData.size();
    }

    @Override
    public void onTypeItemClick(Object object) {
        if(mListener!=null) mListener.onTypeItemClick(object);
    }

    class TrendingHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView mTitle;

        @BindView(R.id.description)
        TextView mDescription;

        @BindView(R.id.recycler_view)
        RecyclerView mRecyclerView;


        public  TrendingHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
        void bind(Index index) {
            mTitle.setText(index.getName());
            mDescription.setText(index.getDescription());
            ItemTrendingAdapter adapter = new ItemTrendingAdapter(mRecyclerView.getContext());
            mRecyclerView.setLayoutManager(adapter.getLayoutManager(mRecyclerView.getContext(),index.getType()));
            mRecyclerView.setAdapter(adapter);
            adapter.setListener(TrendingAdapter.this);

            if(mObjectData.size()>getAdapterPosition()) adapter.setData(mObjectData.get(getAdapterPosition()));
         //   mRecyclerView.setRecycledViewPool(mViewPool);
        }

    }
}
