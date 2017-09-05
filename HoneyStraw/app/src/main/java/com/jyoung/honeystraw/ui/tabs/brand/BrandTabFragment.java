package com.jyoung.honeystraw.ui.tabs.brand;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jyoung.honeystraw.R;
import com.jyoung.honeystraw.model.Brand;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.jyoung.honeystraw.ui.tabs.home.HomeTabRecyclerAdapter.TYPE_BASE;

/**
 * Created by jyoung on 2017. 8. 1..
 */

public class BrandTabFragment extends Fragment {
    @InjectView(R.id.brand_rcv)
    RecyclerView brandRecycler;
    List<Brand> brandList;

    public BrandTabFragment() {
    }

    public static BrandTabFragment getInstance() {
        return new BrandTabFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_brand, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setMockData();

        brandRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        brandRecycler.setAdapter(new BrandRecyclerAdapter(brandList, onClickListener));
    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final int tempPosition = brandRecycler.getChildPosition(view);
            String brandName = brandList.get(tempPosition).getBrandName();
            Intent intent = new Intent(getContext(), BrandDetailActivity.class);
            intent.putExtra("brandName", brandName);
            startActivity(intent);
        }
    };



    public void setMockData(){
        brandList = new ArrayList<Brand>();
        brandList.add(new Brand(TYPE_BASE, null,"맥도날드",R.drawable.macdonald_logo));
        brandList.add(new Brand(TYPE_BASE, null,"다이소",R.drawable.daiso_logo));
        brandList.add(new Brand(TYPE_BASE, null,"롭스",R.drawable.lohb_logo));
        brandList.add(new Brand(TYPE_BASE, null,"서브웨이",R.drawable.subway_logo));
        brandList.add(new Brand(TYPE_BASE, null,"스타벅스",R.drawable.starbucks_logo));
        brandList.add(new Brand(TYPE_BASE, null,"올리브영",R.drawable.oliveyoung_logo));
    }
}
