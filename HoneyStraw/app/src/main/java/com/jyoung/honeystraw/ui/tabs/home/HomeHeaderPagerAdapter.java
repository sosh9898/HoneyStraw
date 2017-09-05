package com.jyoung.honeystraw.ui.tabs.home;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jyoung.honeystraw.R;
import com.jyoung.honeystraw.ui.tabs.brand.BrandDetailActivity;

import java.util.List;

/**
 * Created by jyoung on 2017. 8. 1..
 */

public class HomeHeaderPagerAdapter extends PagerAdapter {
    List<String> baseTipList;
    int[] imageList ={ R.drawable.subway_temp, R.drawable.main_page_item2, R.drawable.main_page_item3} ;
    FragmentActivity tempActivity;

    public HomeHeaderPagerAdapter(List<String> baseTipList, FragmentActivity tempActivity) {
        this.baseTipList = baseTipList;
        this.tempActivity = tempActivity;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.layout_home_best, container, false);
        ImageView imageView = view.findViewById(R.id.best_image);
        Glide.with(tempActivity).load(imageList[position]).into(imageView);
        container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (position){
                    case 0:
                        Intent intent = new Intent(tempActivity, BrandDetailActivity.class);
                        intent.putExtra("brandName", "서브웨이");
                        tempActivity.startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(tempActivity, BrandDetailActivity.class);
                        intent1.putExtra("brandName", "다이소");
                        tempActivity.startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(tempActivity, BrandDetailActivity.class);
                        intent2.putExtra("brandName", "올리브영");
                        tempActivity.startActivity(intent2);
                        break;
                }
            }
        });
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


}


