package com.jyoung.honeystraw.ui.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jyoung.honeystraw.R;
import com.jyoung.honeystraw.application.ApplicationController;
import com.jyoung.honeystraw.base.util.BundleBuilder;
import com.jyoung.honeystraw.base.util.SharedPreferencesService;
import com.jyoung.honeystraw.model.SearchRecently;
import com.jyoung.honeystraw.network.NetworkService;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jyoung.honeystraw.ui.tabs.home.HomeTabRecyclerAdapter.TYPE_HEADER;

/**
 * Created by jyoung on 2017. 8. 4..
 */

public class SearchFragment extends Fragment {
    @InjectView(R.id.search_recently_rcv)RecyclerView searchRecentlyRecycler;
    @InjectView(R.id.empty_item)RelativeLayout emptyItem;
    @InjectView(R.id.empty_image)ImageView emptyImage;
    List<SearchRecently> searchRecentlyList;
    RecentlySearchRecyclerAdapter recentlySearchRecyclerAdapter;

    SharedPreferencesService preferencesService;

    NetworkService service;

    public SearchFragment() {
    }

    public static SearchFragment getInstance(){
        return new SearchFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_recently, null);
        ButterKnife.inject(this, view);
        service = ApplicationController.getInstance().getNetworkService();
        preferencesService = new SharedPreferencesService();
        preferencesService.load(getContext());
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recentlySearchRecyclerAdapter = new RecentlySearchRecyclerAdapter(searchRecentlyList, onClickListener, getContext(), this);
        searchRecentlyRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        searchRecentlyRecycler.setAdapter(recentlySearchRecyclerAdapter);
        getRecentlySearch();

    }

    public void getRecentlySearch(){
        searchRecentlyList = new ArrayList<>();
        searchRecentlyList.add(new SearchRecently(TYPE_HEADER));

        Call<List<SearchRecently>> getRecentlySearch = service.getRecentlySearchList(preferencesService.getPrefStringData("id"));

        getRecentlySearch.enqueue(new Callback<List<SearchRecently>>() {
            @Override
            public void onResponse(Call<List<SearchRecently>> call, Response<List<SearchRecently>> response) {
                if(response.isSuccessful()){
                    searchRecentlyList.addAll(response.body());
                                         recentlySearchRecyclerAdapter.refreshAdapter(searchRecentlyList);


                }
            }

            @Override
            public void onFailure(Call<List<SearchRecently>> call, Throwable t) {

            }
        });
    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final int tempPosition = searchRecentlyRecycler.getChildPosition(view);
            String tempSearchResult = searchRecentlyList.get(tempPosition).getSearchResult();
            ((SearchActivity)getContext()).searchEdit.setText(tempSearchResult);
            ((SearchActivity)getContext()).replaceFragment(new SearchResultFragment(), BundleBuilder.create().with("RecentlySearch", tempSearchResult).build());
            ((SearchActivity)getContext()).searchStart(tempSearchResult);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }
}
