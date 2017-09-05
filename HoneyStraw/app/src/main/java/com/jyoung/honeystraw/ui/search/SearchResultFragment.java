package com.jyoung.honeystraw.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.jyoung.honeystraw.R;
import com.jyoung.honeystraw.application.ApplicationController;
import com.jyoung.honeystraw.base.BaseGridRecyclerAdapter;
import com.jyoung.honeystraw.model.Cover;
import com.jyoung.honeystraw.network.NetworkService;
import com.jyoung.honeystraw.ui.tip.DetailTipActivity;
import com.jyoung.honeystraw.ui.tip.DetailTipVerticalActivity;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.jyoung.honeystraw.ui.register.RegisterFragment.SCROLL_LF;
import static com.jyoung.honeystraw.ui.register.RegisterFragment.SCROLL_UP;

/**
 * Created by jyoung on 2017. 8. 4..
 */

public class SearchResultFragment extends Fragment {
    @InjectView(R.id.search_result_rcv)RecyclerView searchResultRecycler;
    @InjectView(R.id.empty_search_item)RelativeLayout emptyItem;
    @InjectView(R.id.empty_search_image)ImageView emptyImage;

    List<Cover> coverList;
    NetworkService service;

    BaseGridRecyclerAdapter baseAdapter;

    public SearchResultFragment() {
    }

    public static SearchResultFragment getInstance(){
        return new SearchResultFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_result, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        service = ApplicationController.getInstance().getNetworkService();
        if(getArguments() != null) {
            coverList = new ArrayList<Cover>();
            coverList = Parcels.unwrap(getArguments().getParcelable("coverList"));
            setRecycler();
            emptyItem.setVisibility(View.INVISIBLE);
        }
        else{
            Glide.with(getContext()).load(R.drawable.empty_image).into(emptyImage);
            emptyItem.setVisibility(View.VISIBLE);
        }
    }

    public void setRecycler(){
        baseAdapter = new BaseGridRecyclerAdapter(coverList,onClickListener,getContext());
        searchResultRecycler.setLayoutManager(new GridLayoutManager(getContext(),2));
        searchResultRecycler.setHasFixedSize(true);
        searchResultRecycler.setAdapter(baseAdapter);
    }


    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final int tempPosition = searchResultRecycler.getChildAdapterPosition(view);
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
