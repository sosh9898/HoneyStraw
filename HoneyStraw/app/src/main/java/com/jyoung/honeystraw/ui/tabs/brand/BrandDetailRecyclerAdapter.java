package com.jyoung.honeystraw.ui.tabs.brand;

import android.app.Activity;
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


/**
 * Created by jyoung on 2017. 8. 1..
 */

public class BrandDetailRecyclerAdapter extends RecyclerView.Adapter {
    public List<Cover> coverList;
    public View.OnClickListener clickListener;
    public Activity activity;
    public String brandName;
    public int sortType;

    public static final int SORT_TYPE_RECENTLY =1100;
    public static final int SORT_TYPE_VIEWCOUNT =1101;
    public static final int SORT_TYPE_STRAW =1102;
    public static final int SORT_TYPE_COMMENT =1103;

    CustomSortTempDialog customSortTempDialog;


    public BrandDetailRecyclerAdapter(List<Cover> coverList, View.OnClickListener clickListener, Activity activity, String brandName) {
        this.coverList = coverList;
        this.clickListener = clickListener;
        this.activity = activity;
        this.brandName = brandName;
    }

    public void refreshAdapter(List<Cover> coverList){
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
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HomeBaseViewHolder)
            ((HomeBaseViewHolder) holder).bindView(coverList.get(position));
        else if (holder instanceof HomeFullViewHolder)
            ((HomeFullViewHolder) holder).bindView(coverList.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return coverList.get(position).getCoverType();
    }

    @Override
    public int getItemCount() {
        return coverList != null ? coverList.size() : 0;
    }


}
