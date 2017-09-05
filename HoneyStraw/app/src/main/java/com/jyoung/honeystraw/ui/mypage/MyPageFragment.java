package com.jyoung.honeystraw.ui.mypage;

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
import com.jyoung.honeystraw.base.util.SharedPreferencesService;
import com.jyoung.honeystraw.model.Cover;
import com.jyoung.honeystraw.model.UserPage;
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
 * Created by jyoung on 2017. 8. 3..
 */

public class MyPageFragment extends Fragment {
    @InjectView(R.id.home_rcv)RecyclerView myPageRecycler;
    @InjectView(R.id.refresh_layout)PullToRefreshView refreshView;
    GridLayoutManager gridLayoutManager;
    List<Cover> coverList;
    UserPage.ResultData result;
    MyPageRecyclerAdapter myPageRecyclerAdapter;
    NetworkService service;

    SharedPreferencesService preferencesService;

    String tempId;

    public MyPageFragment(){}

    public static MyPageFragment getInstance(){
        return new MyPageFragment();
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
        tempId = getArguments().getString("id");
        preferencesService = new SharedPreferencesService();
        preferencesService.load(getContext());
        setRefresh();
        setRecycler();
        networking();
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

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final int tempPosition = myPageRecycler.getChildAdapterPosition(view);
            Intent intent;
            if(coverList.get(tempPosition).getCoverType() != TYPE_HEADER) {
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
        }
    };



    public void networking() {
        service = ApplicationController.getInstance().getNetworkService();

        Call<UserPage> getUserPage = service.getUserInfo(tempId);

        getUserPage.enqueue(new Callback<UserPage>() {
            @Override
            public void onResponse(Call<UserPage> call, Response<UserPage> response) {
                if (response.isSuccessful()) {
                    coverList = new ArrayList<Cover>();
                    coverList.add(new Cover(TYPE_HEADER));
                    result = response.body().result;
                    coverList.addAll(result.tipList);
                    myPageRecyclerAdapter.refreshAdapter(result.userInfo, coverList);
                }
            }

            @Override
            public void onFailure(Call<UserPage> call, Throwable t) {
                Log.e("userinfo_error", "error : " + t.toString());
            }
        });

    }


    public void setRecycler(){
        myPageRecyclerAdapter = new MyPageRecyclerAdapter(coverList,onClickListener,getContext());
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(coverList.get(position).getCoverType() == TYPE_HEADER) return 2;
                else return 1;
            }
        });

        myPageRecycler.setLayoutManager(gridLayoutManager);
        myPageRecycler.setHasFixedSize(true);
        myPageRecycler.setAdapter(myPageRecyclerAdapter);
    }


    @Override
    public void onResume() {
        super.onResume();
        networking();
    }
}
