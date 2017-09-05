package com.jyoung.honeystraw.ui.search;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jyoung.honeystraw.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by jyoung on 2017. 8. 1..
 */

public class AutoSearchRecyclerAdapter extends RecyclerView.Adapter {
    public List<String> searchAutoList;
    public View.OnClickListener onClickListener;
    public Activity activity;


    public AutoSearchRecyclerAdapter(List<String> searchAutoList, View.OnClickListener onClickListener, Activity getActivity) {
        this.searchAutoList = searchAutoList;
        this.onClickListener = onClickListener;
        this.activity = getActivity;
    }

    public void refreshAdapter(List<String> searchAutoList) {
        this.searchAutoList = searchAutoList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_search_recently_baseitem, parent, false);
        view.setOnClickListener(onClickListener);
      return new RecentlySearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((RecentlySearchViewHolder) holder).bindView(searchAutoList.get(position));
    }


    @Override
    public int getItemCount() {
        return searchAutoList != null ? searchAutoList.size() : 0;
    }

    public class RecentlySearchViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.search_recently_text)
        TextView searchRecentlyText;

        public RecentlySearchViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }

        public void bindView(String searchText) {
            SpannableStringBuilder builder = new SpannableStringBuilder(searchText);
            builder.setSpan(new ForegroundColorSpan(Color.parseColor("#FFC107")), 0, ((SearchActivity)activity).getLength(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            searchRecentlyText.setText(builder);

        }
    }

}