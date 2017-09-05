package com.jyoung.honeystraw.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jyoung.honeystraw.R;
import com.jyoung.honeystraw.application.ApplicationController;
import com.jyoung.honeystraw.base.util.ToastMaker;
import com.jyoung.honeystraw.model.Cover;
import com.jyoung.honeystraw.network.NetworkService;
import com.jyoung.honeystraw.ui.tabs.home.HomeTabRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by jyoung on 2017. 8. 3..
 */

public class BaseListFragment extends Fragment {
    @InjectView(R.id.home_rcv)RecyclerView baseRecycler;
    List<Cover> coverList;
    NetworkService service;
    String brandName;
    HomeTabRecyclerAdapter homeTabRecyclerAdapter;

    public BaseListFragment(){}

    public static BaseListFragment getInstance(){
        return new BaseListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        if(getArguments() != null)
        setBrand(getArguments());
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
        service = ApplicationController.getInstance().getNetworkService();
        coverList = new ArrayList<Cover>();
        setRecycler();
    }

    public void setRecycler(){
        baseRecycler.setLayoutManager(new GridLayoutManager(getContext(),2));
        baseRecycler.setHasFixedSize(true);
        baseRecycler.setAdapter(new HomeTabRecyclerAdapter(coverList,onClickListener,BaseListFragment.this,getActivity()));
    }



    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final int tempPosition = baseRecycler.getChildAdapterPosition(view);
            ToastMaker.makeShortToast(getContext(),(tempPosition+1)+"번 클릭");
        }
    };

    public void setBrand(Bundle bundle){
        brandName = bundle.getString("brandName");
        String[] tempBrand =getResources().getStringArray(R.array.brandName);
        for(String str : tempBrand){
            if(str.equals(brandName)) ToastMaker.makeShortToast(getContext(), str);
        }
    }
}
