package com.jyoung.honeystraw.ui.tabs.straw;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.jyoung.honeystraw.R;
import com.jyoung.honeystraw.application.ApplicationController;
import com.jyoung.honeystraw.base.BaseGridRecyclerAdapter;
import com.jyoung.honeystraw.base.util.SharedPreferencesService;
import com.jyoung.honeystraw.model.Cover;
import com.jyoung.honeystraw.network.NetworkService;
import com.jyoung.honeystraw.ui.tip.DetailTipActivity;
import com.jyoung.honeystraw.ui.tip.DetailTipVerticalActivity;
import com.song.refresh_view.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jyoung.honeystraw.ui.register.RegisterFragment.SCROLL_LF;
import static com.jyoung.honeystraw.ui.register.RegisterFragment.SCROLL_UP;
import static com.jyoung.honeystraw.ui.tabs.home.HomeTabRecyclerAdapter.TYPE_HEADER;

/**
 * Created by jyoung on 2017. 8. 1..
 */

public class StrawTabFragment extends Fragment{
    @InjectView(R.id.home_rcv)RecyclerView baseRecycler;
    @InjectView(R.id.refresh_layout)PullToRefreshView refreshView;
    List<Cover> coverList;
    NetworkService service;
    BaseGridRecyclerAdapter baseAdapter;
    GridLayoutManager gridLayoutManager;

    SharedPreferencesService preferencesService;

    int flag;

    public StrawTabFragment() {
    }

    public static StrawTabFragment getInstance(){
        return new StrawTabFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_home, null);
            return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);

        preferencesService = new SharedPreferencesService();
        preferencesService.load(getContext());
        service = ApplicationController.getInstance().getNetworkService();
        coverList = new ArrayList<Cover>();
        setRecycler();
        networking();
        setRefresh();
    }

    public void setRefresh() {
        refreshView.setColorSchemeColors(getResources().getColor(R.color.colorPrimary), Color.BLUE);
        refreshView.setSmileStrokeWidth(14);
        refreshView.setSmileInterpolator(new LinearInterpolator());
        refreshView.setSmileAnimationDuration(2000);
        refreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                networking();
                refreshView.setRefreshing(false);
            }

        });
    }
    public void setRecycler(){
        baseAdapter = new BaseGridRecyclerAdapter(coverList,onClickListener,getContext());
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (coverList.get(position).getCoverType() == TYPE_HEADER) return 2;
                else return 1;
            }
        });
        baseRecycler.setLayoutManager(gridLayoutManager);
        baseRecycler.setHasFixedSize(true);
        baseRecycler.setAdapter(baseAdapter);
    }

    public void networking(){
        Call<List<Cover>> getStrawList = service.getStrawList(preferencesService.getPrefStringData("id"));

        getStrawList.enqueue(new Callback<List<Cover>>() {
            @Override
            public void onResponse(Call<List<Cover>> call, Response<List<Cover>> response) {
                if(response.isSuccessful()) {
                    coverList = response.body();
                    baseAdapter.refreshAdapter(coverList);
                }
            }

            @Override
            public void onFailure(Call<List<Cover>> call, Throwable t) {
                Log.d("err" , t.toString());
            }
        });
    }


    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final int tempPosition = baseRecycler.getChildAdapterPosition(view);
            Intent intent;
            if (coverList.get(tempPosition).getScrollType() == SCROLL_LF) {
                intent = new Intent(getContext(), DetailTipActivity.class);
                intent.putExtra("background", coverList.get(tempPosition).getBackgroundImage());
                intent.putExtra("num", coverList.get(tempPosition).getCoverNum());
                intent.putExtra("userId", coverList.get(tempPosition).getUserId());
                intent.putExtra("title", coverList.get(tempPosition).getCoverContent());
                intent.putExtra("state", coverList.get(tempPosition).getStrawState());
                intent.putExtra("strawNum", coverList.get(tempPosition).getStrawNum());
                intent.putExtra("commentNum", coverList.get(tempPosition).getCommentNum());
                intent.putExtra("brandName", coverList.get(tempPosition).getBrand());
                intent.putExtra("postdate", coverList.get(tempPosition).getPostdate());
                intent.putExtra("viewCount", coverList.get(tempPosition).getViewCount());
                startActivity(intent);
            } else if (coverList.get(tempPosition).getScrollType() == SCROLL_UP) {
                intent = new Intent(getContext(), DetailTipVerticalActivity.class);
                intent.putExtra("background", coverList.get(tempPosition).getBackgroundImage());
                intent.putExtra("num", coverList.get(tempPosition).getCoverNum());
                intent.putExtra("userId", coverList.get(tempPosition).getUserId());
                intent.putExtra("title", coverList.get(tempPosition).getCoverContent());
                intent.putExtra("state", coverList.get(tempPosition).getStrawState());
                intent.putExtra("strawNum", coverList.get(tempPosition).getStrawNum());
                intent.putExtra("commentNum", coverList.get(tempPosition).getCommentNum());
                intent.putExtra("brandName", coverList.get(tempPosition).getBrand());
                intent.putExtra("postdate", coverList.get(tempPosition).getPostdate());
                intent.putExtra("viewCount", coverList.get(tempPosition).getViewCount());
                startActivity(intent);
            }
        }
    };

}
