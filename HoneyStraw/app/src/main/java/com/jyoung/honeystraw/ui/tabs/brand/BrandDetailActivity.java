package com.jyoung.honeystraw.ui.tabs.brand;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jyoung.honeystraw.R;
import com.jyoung.honeystraw.application.ApplicationController;
import com.jyoung.honeystraw.model.Cover;
import com.jyoung.honeystraw.network.NetworkService;
import com.jyoung.honeystraw.ui.tip.DetailTipActivity;
import com.jyoung.honeystraw.ui.tip.DetailTipVerticalActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jyoung.honeystraw.ui.register.RegisterFragment.SCROLL_LF;
import static com.jyoung.honeystraw.ui.register.RegisterFragment.SCROLL_UP;
import static com.jyoung.honeystraw.ui.tabs.brand.BrandDetailRecyclerAdapter.SORT_TYPE_COMMENT;
import static com.jyoung.honeystraw.ui.tabs.brand.BrandDetailRecyclerAdapter.SORT_TYPE_RECENTLY;
import static com.jyoung.honeystraw.ui.tabs.brand.BrandDetailRecyclerAdapter.SORT_TYPE_STRAW;
import static com.jyoung.honeystraw.ui.tabs.brand.BrandDetailRecyclerAdapter.SORT_TYPE_VIEWCOUNT;
import static com.jyoung.honeystraw.ui.tabs.home.HomeTabRecyclerAdapter.TYPE_HEADER;

public class BrandDetailActivity extends AppCompatActivity {
    @InjectView(R.id.brand_toolbar)Toolbar brandToolbar;
    @InjectView(R.id.brand_detail_rcv)RecyclerView brandDetailRecycler;
    String brandName;
    List<Cover> coverList;
    NetworkService service;
    BrandDetailRecyclerAdapter brandDetailRecyclerAdapter;
    GridLayoutManager gridLayoutManager;
    int sortType;

    CustomSortTempDialog customSortTempDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_detail);
        ButterKnife.inject(this);
        initData();
        setToolbar(brandName);
        sortType = SORT_TYPE_RECENTLY;
        networking(sortType);
        setRecycler();
        customSortTempDialog = new CustomSortTempDialog(BrandDetailActivity.this, recentlyListener, viewcountListener, strawListener, commentListener);

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
    }

    public void initData(){
        service = ApplicationController.getInstance().getNetworkService();
        Intent getData = getIntent();
        brandName = getData.getExtras().getString("brandName");
    }

    public void setToolbar(String brandName){
        brandToolbar.setTitle(brandName);
        brandToolbar.setTitleTextColor(getResources().getColor(R.color.splashBackground));
        setSupportActionBar(brandToolbar);
        brandToolbar.setNavigationIcon(R.drawable.left_arrow);
        brandToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    public View.OnClickListener recentlyListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            sortType = SORT_TYPE_RECENTLY;
            networking(sortType);
            customSortTempDialog.dismiss();
        }
    };
    public View.OnClickListener viewcountListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            sortType = SORT_TYPE_VIEWCOUNT;
            networking(sortType);
            customSortTempDialog.dismiss();
        }
    };
    public View.OnClickListener strawListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            sortType = SORT_TYPE_STRAW;
            networking(sortType);
            customSortTempDialog.dismiss();
        }
    };
    public View.OnClickListener commentListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            sortType = SORT_TYPE_COMMENT;
            networking(sortType);
            customSortTempDialog.dismiss();
        }
    };

    public String returnBrandName(){
        return brandName;
    }

    public void setRecycler(){
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (coverList.get(position).getCoverType() == TYPE_HEADER) return 2;
                else return 1;
            }
        });
        brandDetailRecycler.setLayoutManager(gridLayoutManager);
        brandDetailRecyclerAdapter = new BrandDetailRecyclerAdapter(coverList, onClickListener,BrandDetailActivity.this,brandName);
        brandDetailRecycler.setHasFixedSize(true);
        brandDetailRecycler.setAdapter(brandDetailRecyclerAdapter);
    }

    public void networking(int sortType) {
        coverList = new ArrayList<>();
        Call<List<Cover>> getDetailList = service.getDetailList(brandName, sortType);

        getDetailList.enqueue(new Callback<List<Cover>>() {
            @Override
            public void onResponse(Call<List<Cover>> call, Response<List<Cover>> response) {
                if (response.isSuccessful()) {
                    coverList.addAll(response.body());
                    brandDetailRecyclerAdapter.refreshAdapter(coverList);
                }
            }

            @Override
            public void onFailure(Call<List<Cover>> call, Throwable t) {
                Log.d("error" ,"error + "+toString());
            }
        });
    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final int tempPosition = brandDetailRecycler.getChildAdapterPosition(view);
            Intent intent;
            if(tempPosition != 0) {
                if (coverList.get(tempPosition).getScrollType() == SCROLL_LF) {
                    intent = new Intent(getApplicationContext(), DetailTipActivity.class);
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
                    intent = new Intent(getApplicationContext(), DetailTipVerticalActivity.class);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sort_setting) {
            customSortTempDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.brand, menu);
        return true;
    }

}
