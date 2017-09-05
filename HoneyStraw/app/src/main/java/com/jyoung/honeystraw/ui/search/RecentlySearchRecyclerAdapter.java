package com.jyoung.honeystraw.ui.search;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jyoung.honeystraw.R;
import com.jyoung.honeystraw.application.ApplicationController;
import com.jyoung.honeystraw.base.util.SharedPreferencesService;
import com.jyoung.honeystraw.model.RequestResult;
import com.jyoung.honeystraw.model.SearchRecently;
import com.jyoung.honeystraw.network.NetworkService;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jyoung.honeystraw.ui.tabs.home.HomeTabRecyclerAdapter.TYPE_BASE;
import static com.jyoung.honeystraw.ui.tabs.home.HomeTabRecyclerAdapter.TYPE_HEADER;


/**
 * Created by jyoung on 2017. 8. 1..
 */

public class RecentlySearchRecyclerAdapter extends RecyclerView.Adapter {
    public List<SearchRecently> searchRecentlyList;
    public View.OnClickListener onClickListener;
    NetworkService service;
    SharedPreferencesService preferencesService;
    Context context;
    Fragment fragment;

    public RecentlySearchRecyclerAdapter(List<SearchRecently> searchRecentlyList, View.OnClickListener onClickListener, Context context, Fragment fragment) {
        this.searchRecentlyList = searchRecentlyList;
        this.onClickListener = onClickListener;
        this.context = context;
        this.fragment = fragment;
    }

    public void refreshAdapter(List<SearchRecently> searchRecentlyList) {
        this.searchRecentlyList = searchRecentlyList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_HEADER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_search_recently_headeritem, parent, false);
                view.setOnClickListener(onClickListener);
                return new SearchHeaderViewHolder(view);
            case TYPE_BASE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_search_recently_baseitem, parent, false);
                view.setOnClickListener(onClickListener);
                return new RecentlySearchViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SearchHeaderViewHolder) {
        } else if (holder instanceof RecentlySearchViewHolder)
            ((RecentlySearchViewHolder) holder).bindView(searchRecentlyList.get(position));
    }


    @Override
    public int getItemCount() {
        return searchRecentlyList != null ? searchRecentlyList.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return searchRecentlyList.get(position).getSearchType();

    }

    public class RecentlySearchViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.search_recently_text)
        TextView searchRecentlyText;

        public RecentlySearchViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }

        public void bindView(SearchRecently searchRecently) {
            searchRecentlyText.setText(searchRecently.getSearchResult());
        }
    }

    public class SearchHeaderViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.delete_recently_record)
        TextView deleteRecord;

        public SearchHeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            service = ApplicationController.getInstance().getNetworkService();
            preferencesService = new SharedPreferencesService();
            preferencesService.load(context);
        }

        @OnClick(R.id.delete_recently_record)
        void onClick(View view) {
            networking();
        }
    }

    public void networking() {
        Call<RequestResult> deleteRecord = service.getRecordReset(preferencesService.getPrefStringData("id"));

        deleteRecord.enqueue(new Callback<RequestResult>() {
            @Override
            public void onResponse(Call<RequestResult> call, Response<RequestResult> response) {
                if(response.isSuccessful()){
                    if(response.body().message.equals("ok")){
                        ((SearchFragment)fragment).getRecentlySearch();
                    }
                }
            }

            @Override
            public void onFailure(Call<RequestResult> call, Throwable t) {

            }
        });
    }
}