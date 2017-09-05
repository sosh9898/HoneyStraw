package com.jyoung.honeystraw.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jyoung.honeystraw.R;
import com.jyoung.honeystraw.model.Cover;
import com.jyoung.honeystraw.ui.tabs.home.HomeBaseViewHolder;
import com.jyoung.honeystraw.ui.tabs.home.HomeFullViewHolder;

import java.util.List;

import static com.jyoung.honeystraw.ui.tabs.home.HomeTabRecyclerAdapter.TYPE_BASE;
import static com.jyoung.honeystraw.ui.tabs.home.HomeTabRecyclerAdapter.TYPE_FULL;
import static com.jyoung.honeystraw.ui.tabs.home.HomeTabRecyclerAdapter.TYPE_HEADER;


/**
 * Created by jyoung on 2017. 8. 1..
 */

public class BaseGridRecyclerAdapter extends RecyclerView.Adapter {
    public List<Cover> coverList;
    public View.OnClickListener clickListener;
    public Context context;

    public BaseGridRecyclerAdapter(List<Cover> coverList, View.OnClickListener clickListener, Context context) {
        this.coverList = coverList;
        this.clickListener = clickListener;
        this.context = context;
    }


    public void refreshAdapter(List<Cover> coverList) {
        this.coverList = coverList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_FULL:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_home_baseitem_full, parent, false);
                view.setOnClickListener(clickListener);
                return new HomeFullViewHolder(view);
            case TYPE_BASE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_home_baseitem, parent, false);
                view.setOnClickListener(clickListener);
                return new HomeBaseViewHolder(view);
            case TYPE_HEADER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_straw_headeritem, parent, false);
                return new StrawHeaderViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HomeBaseViewHolder)
            ((HomeBaseViewHolder) holder).bindView(coverList.get(position));
        else if (holder instanceof HomeFullViewHolder)
            ((HomeFullViewHolder) holder).bindView(coverList.get(position));
        else{}

    }

    @Override
    public int getItemCount() {
        return coverList != null ? coverList.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return coverList.get(position).getCoverType();
    }

    public class StrawHeaderViewHolder extends RecyclerView.ViewHolder {

        public StrawHeaderViewHolder(View itemView) {
            super(itemView);
        }
    }
}
