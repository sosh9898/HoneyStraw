package com.jyoung.honeystraw.ui.tabs.home;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imbryk.viewPager.LoopViewPager;
import com.jyoung.honeystraw.R;
import com.jyoung.honeystraw.model.Cover;
import com.jyoung.honeystraw.ui.tabs.brand.CustomSortTempDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.relex.circleindicator.CircleIndicator;


/**
 * Created by jyoung on 2017. 8. 1..
 */

public class HomeTabRecyclerAdapter extends RecyclerView.Adapter {
    public List<Cover> coverList;
    public View.OnClickListener clickListener;
    public static final int TYPE_BASE = 101;
    public static final int TYPE_HEADER = 100;
    public static final int TYPE_FOOTER = 102;
    public static final int TYPE_FULL = 103;
    public static final int STRAW_CHECKED = 901;
    public static final int STRAW_UNCHECKED = 900;

    public Fragment context;
    FragmentActivity tempActivity;
    int sortType;
    CustomSortTempDialog customSortTempDialog;

    public HomeTabRecyclerAdapter(List<Cover> coverList, View.OnClickListener clickListener, Fragment context, FragmentActivity tempActivity) {
        this.coverList = coverList;
        this.clickListener = clickListener;
        this.context = context;
        this.tempActivity = tempActivity;
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
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_home_headeritem, parent, false);
                view.setOnClickListener(clickListener);
                return new HomeHeaderViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HomeBaseViewHolder)
            ((HomeBaseViewHolder) holder).bindView(coverList.get(position));
        else if (holder instanceof HomeFullViewHolder)
            ((HomeFullViewHolder) holder).bindView(coverList.get(position));
        else if (holder instanceof HomeHeaderViewHolder)
            ((HomeHeaderViewHolder) holder).bindView();
    }

    @Override
    public int getItemViewType(int position) {
        return coverList.get(position).getCoverType();
    }

    @Override
    public int getItemCount() {
        return coverList != null ? coverList.size() : 0;
    }

    public class HomeHeaderViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.home_header_viewpager)
        LoopViewPager viewPager;
        @InjectView(R.id.home_header_indicator)
        CircleIndicator indicator;
        final int NUM_PAGE = 3;
        int currentPage;


        public HomeHeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }

        public void bindView() {
            List<String> bestTipList = new ArrayList<>();
            viewPager.setAdapter(new HomeHeaderPagerAdapter(bestTipList, tempActivity));
            indicator.setViewPager(viewPager);

            currentPage = viewPager.getCurrentItem();


                final Handler handler = new Handler();

                final Runnable update = new Runnable() {
                    public void run() {
                        if (currentPage == NUM_PAGE) {
                            currentPage = 0;
                        }
                        viewPager.setCurrentItem(currentPage++, true);
                    }
                };


                new Timer().schedule(new TimerTask() {

                    @Override
                    public void run() {
                        handler.post(update);
                    }
                }, 100, 4000);

        }

    }

}
