package com.jyoung.honeystraw.ui.tabs.home;

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
import com.jyoung.honeystraw.network.NetworkService;
import com.jyoung.honeystraw.ui.mypage.CustomHideTipDialog;
import com.jyoung.honeystraw.ui.tabs.TabActivity;
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
import static com.jyoung.honeystraw.ui.tabs.brand.BrandDetailRecyclerAdapter.SORT_TYPE_RECENTLY;
import static com.jyoung.honeystraw.ui.tabs.home.HomeTabRecyclerAdapter.TYPE_HEADER;

/**
 * Created by jyoung on 2017. 8. 1..
 */

public class HomeTabFragment extends Fragment {
    @InjectView(R.id.home_rcv)
    RecyclerView homeRecycler;
    @InjectView(R.id.refresh_layout)
    PullToRefreshView refreshView;
    HomeTabRecyclerAdapter homeTabRecyclerAdapter;
    GridLayoutManager gridLayoutManager;
    List<Cover> coverList;

    int startIndex = 0;
    final int GETTING_DATA_COUNT = 10;
    SharedPreferencesService preferencesService;

    CustomHideTipDialog customHideTipDialog;

    NetworkService service;
    int sortType;

    public HomeTabFragment() {
    }

    public static HomeTabFragment getInstance() {
        return new HomeTabFragment();
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
        service = ApplicationController.getInstance().getNetworkService();
        preferencesService = new SharedPreferencesService();
        preferencesService.load(getContext());
        coverList = new ArrayList<Cover>();
        coverList.add(new Cover(TYPE_HEADER));
        networking(SORT_TYPE_RECENTLY, 0, 0);
        setRefresh();
        setHomeRecycler();

    }

    @Override
    public void onResume() {
        super.onResume();
        networking(sortType,0, 0);
        startIndex = 0;
    }

    public  void setStartIndex(){
        startIndex = 0;
    }
    public void setRefresh() {
        refreshView.setColorSchemeColors(getResources().getColor(R.color.colorPrimary), Color.BLUE);
        refreshView.setSmileStrokeWidth(14);
        refreshView.setSmileInterpolator(new LinearInterpolator());
        refreshView.setSmileAnimationDuration(2000);
        refreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setHomeRecycler();
                networking(sortType, 0, 0);
                refreshView.setRefreshing(false);
                startIndex = 0;
            }

        });
    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final int tempPosition = homeRecycler.getChildAdapterPosition(view);
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

    public void networking(int sortType,final int flag, int startIndex) {
        this.sortType = sortType;
        if (!preferencesService.getPrefStringData("id").equals("")) {
            Call<List<Cover>> getMainList = service.getMainListAfterLogin(preferencesService.getPrefStringData("id"), sortType, startIndex);

            getMainList.enqueue(new Callback<List<Cover>>() {
                @Override
                public void onResponse(Call<List<Cover>> call, Response<List<Cover>> response) {
                    if (response.isSuccessful()) {
                        if (flag == 0) {
                            coverList = new ArrayList<Cover>();
                            coverList.add(new Cover(TYPE_HEADER));
                            coverList.addAll(response.body());
                            setHomeRecycler();
                        } else {
                            Log.d("?","???");
                            coverList.addAll(response.body());
                            Log.d("df",coverList.get(11).getCoverImage());

                            homeTabRecyclerAdapter.refreshAdapter(coverList);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Cover>> call, Throwable t) {

                }
            });
        } else {
            Call<List<Cover>> getMainList = service.getMainList(sortType, startIndex);

            getMainList.enqueue(new Callback<List<Cover>>() {
                @Override
                public void onResponse(Call<List<Cover>> call, Response<List<Cover>> response) {
                    if (response.isSuccessful()) {
                        if (flag == 0) {
                            coverList = new ArrayList<Cover>();
                            coverList.add(new Cover(TYPE_HEADER));
                            coverList.addAll(response.body());
                            setHomeRecycler();
                        } else {
                            coverList.addAll(response.body());
                            homeTabRecyclerAdapter.refreshAdapter(coverList);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Cover>> call, Throwable t) {

                }
            });
        }
    }


    public void setHomeRecycler() {
        sortType = SORT_TYPE_RECENTLY;
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (coverList.get(position).getCoverType() == TYPE_HEADER) return 2;
                else return 1;
            }
        });
        homeRecycler.setLayoutManager(gridLayoutManager);
        homeTabRecyclerAdapter = new HomeTabRecyclerAdapter(coverList, onClickListener, HomeTabFragment.this, getActivity());
        homeRecycler.setHasFixedSize(true);
        homeRecycler.setAdapter(homeTabRecyclerAdapter);


        homeRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0 ||dy<0 && ((TabActivity)getActivity()).fab.isShown()) {
                    ((TabActivity) getActivity()).fab.hide();
                }
            }
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                    int lastPosition = ((GridLayoutManager) manager).findLastVisibleItemPosition();
                    if ((lastPosition + 2) >= (coverList.size() - 1) &&
                            startIndex + GETTING_DATA_COUNT == coverList.size() - 1) {

                        startIndex = coverList.size()-1;  //헤더를 제외한 리스트 크기

                        networking(sortType, 1, startIndex); //startIndex 부터 10개의 데이터를 받아아옴
                    }
                }else if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    ((TabActivity)getActivity()).fab.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }
}



