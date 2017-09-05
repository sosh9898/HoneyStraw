package com.jyoung.honeystraw.ui.tabs.notify;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
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
import com.jyoung.honeystraw.model.Notify;
import com.jyoung.honeystraw.model.RequestResult;
import com.jyoung.honeystraw.network.NetworkService;
import com.jyoung.honeystraw.ui.tip.DetailTipActivity;
import com.jyoung.honeystraw.ui.tip.DetailTipCommentActivity;
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
import static com.jyoung.honeystraw.ui.tabs.notify.NotificationRecyclerAdapter.NOTIFY_COMMENT_LIKE;
import static com.jyoung.honeystraw.ui.tabs.notify.NotificationRecyclerAdapter.NOTIFY_COMMENT_REGISTER;
import static com.jyoung.honeystraw.ui.tabs.notify.NotificationRecyclerAdapter.NOTIFY_TIP_LIKE;

/**
 * Created by jyoung on 2017. 8. 20..
 */

public class NotificationFragment extends Fragment {
    @InjectView(R.id.notify_rcv)
    RecyclerView notifyRecycler;
    NotificationRecyclerAdapter notificationRecyclerAdapter;
    @InjectView(R.id.refresh_layout_notify)PullToRefreshView refreshView;

    int flag = 0;
    Cover cover;
    List<Notify> notifyList;
    NetworkService service;
    SharedPreferencesService preferencesService;
    int startIndex =0;
    CustomHideNotifyDialog customHideNotifyDialog;


    public NotificationFragment() {
    }

    public static NotificationFragment getInstance() {
        return new NotificationFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notify, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        service = ApplicationController.getInstance().getNetworkService();
        preferencesService = new SharedPreferencesService();
        preferencesService.load(getContext());
        notifyRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                    int lastPosition = ((LinearLayoutManager) manager).findLastVisibleItemPosition();

                    Log.d("fxfx",(lastPosition + 2) + ">="+(notifyList.size() - 1));
                    Log.d("fxfx",startIndex + 10 +"=="+notifyList.size());

                    if ((lastPosition + 2) >= (notifyList.size() - 1) && startIndex + 10 == notifyList.size()) {
                        startIndex = notifyList.size();
                        networking(startIndex, 1);
                    }
                }

            }
        });
        setRecycler();
        networking(startIndex, 0);
        setRefresh();
    }

    public void setRecycler() {
        notifyList = new ArrayList<>();
        notificationRecyclerAdapter = new NotificationRecyclerAdapter(notifyList, onClickListener, NotificationFragment.this, getContext());
        notifyRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        notifyRecycler.setHasFixedSize(true);
        notifyRecycler.setAdapter(notificationRecyclerAdapter);



    }

    public void setRefresh() {
        refreshView.setColorSchemeColors(getResources().getColor(R.color.colorPrimary), Color.BLUE);
        refreshView.setSmileStrokeWidth(14);
        refreshView.setSmileInterpolator(new LinearInterpolator());
        refreshView.setSmileAnimationDuration(2000);
        refreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startIndex = 0;
                networking(startIndex, 0);
                refreshView.setRefreshing(false);
            }

        });
    }

    public void networking(int startIndex, final int flag) {
        Call<List<Notify>> getNotification = service.getNotification(preferencesService.getPrefStringData("id"), startIndex);

        getNotification.enqueue(new Callback<List<Notify>>() {
            @Override
            public void onResponse(Call<List<Notify>> call, Response<List<Notify>> response) {
                if (response.isSuccessful()) {
                    if(flag == 0){
                        Log.d("fdfd", response.body().size()+"");
                        notifyList = response.body();
                        notificationRecyclerAdapter.refreshAdapter(notifyList);
                    }
                    else {
                        notifyList.addAll(response.body());
                        notificationRecyclerAdapter.refreshAdapter(notifyList);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Notify>> call, Throwable t) {

            }
        });



    }


    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final int tempPosition = notifyRecycler.getChildPosition(view);

            if (notifyList.get(tempPosition).getCheck() == 0)
                notifyCheckUpdate(tempPosition);

            switch (notifyList.get(tempPosition).getNotifyType()) {
                case NOTIFY_TIP_LIKE:
                    detailTip(tempPosition);
                    break;
                case NOTIFY_COMMENT_LIKE:
                    detailComment(tempPosition);
                    break;
                case NOTIFY_COMMENT_REGISTER:
                    detailComment(tempPosition);
                    break;
            }
        }
    };

    public void notifyCheckUpdate(final int tempPosition) {
        Call<RequestResult> updateNotify = service.updateCheckNotify(notifyList.get(tempPosition).getNum());

        updateNotify.enqueue(new Callback<RequestResult>() {
            @Override
            public void onResponse(Call<RequestResult> call, Response<RequestResult> response) {
                if (response.isSuccessful()) {
                    if (response.body().message.equals("ok")){
                        notifyList.get(tempPosition).setCheck(1);
                        notificationRecyclerAdapter.notifyItemChanged(tempPosition);

                    }

                }
            }

            @Override
            public void onFailure(Call<RequestResult> call, Throwable t) {
            }
        });
    }

    public void detailTip(int tempPosition) {
        cover = new Cover();
        Call<Cover> getCoverInfo = service.getTipInfoNotify(preferencesService.getPrefStringData("id"), notifyList.get(tempPosition).getCoverNum());
        getCoverInfo.enqueue(new Callback<Cover>() {
            @Override
            public void onResponse(Call<Cover> call, Response<Cover> response) {
                if (response.isSuccessful()) {
                    cover = response.body();
                    Intent intent;
                    Log.d("dddd", cover.getScrollType() + "");

                    if (cover.getScrollType() == SCROLL_LF) {
                        Log.d("dddd", "???");
                        intent = new Intent(getContext(), DetailTipActivity.class);
                        intent.putExtra("background", cover.getBackgroundImage());
                        intent.putExtra("num", cover.getCoverNum());
                        intent.putExtra("userId", cover.getUserId());
                        intent.putExtra("title", cover.getCoverContent());
                        intent.putExtra("state", cover.getStrawState());
                        intent.putExtra("strawNum", cover.getStrawNum());
                        intent.putExtra("commentNum", cover.getCommentNum());
                        intent.putExtra("brandName", cover.getBrand());
                        intent.putExtra("postdate", cover.getPostdate());
                        intent.putExtra("viewCount", cover.getViewCount());
                        getActivity().startActivity(intent);
                    } else if (cover.getScrollType() == SCROLL_UP) {
                        Log.d("dddd", "????");
                        intent = new Intent(getContext(), DetailTipVerticalActivity.class);
                        intent.putExtra("background", cover.getBackgroundImage());
                        intent.putExtra("num", cover.getCoverNum());
                        intent.putExtra("userId", cover.getUserId());
                        intent.putExtra("title", cover.getCoverContent());
                        intent.putExtra("state", cover.getStrawState());
                        intent.putExtra("strawNum", cover.getStrawNum());
                        intent.putExtra("commentNum", cover.getCommentNum());
                        intent.putExtra("brandName", cover.getBrand());
                        intent.putExtra("postdate", cover.getPostdate());
                        intent.putExtra("viewCount", cover.getViewCount());
                        getActivity().startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<Cover> call, Throwable t) {
                Log.d("notifyDetailError", t.toString());
            }
        });
    }


    public void detailComment(int tempPosition) {
        Intent intent = new Intent(getContext(), DetailTipCommentActivity.class);
        intent.putExtra("coverNum", notifyList.get(tempPosition).getCoverNum());
        getActivity().startActivity(intent);
    }

    public void setStartIndex(){
        startIndex = 0;
    }

}
