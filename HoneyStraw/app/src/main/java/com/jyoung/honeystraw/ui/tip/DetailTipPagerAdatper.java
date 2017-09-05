package com.jyoung.honeystraw.ui.tip;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.jyoung.honeystraw.base.util.BundleBuilder;
import com.jyoung.honeystraw.model.DetailTips;
import com.jyoung.honeystraw.model.TipTemplate;

/**
 * Created by jyoung on 2017. 8. 1..
 */

public class DetailTipPagerAdatper extends FragmentStatePagerAdapter {
    SparseArray<Fragment> detailTipFragments = new SparseArray<Fragment>();
    int count;
    DetailTips.ResultData resultData;
    TipTemplate template;

    public DetailTipPagerAdatper(FragmentManager fm, int count, DetailTips.ResultData resultData) {
        super(fm);
        this.count = count;
        this.resultData = resultData;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return DetailCoverFragment.getInstance(BundleBuilder.create().with("tipFirstPage", resultData.userInfo).build());
            default:
                template = resultData.tipContents.get(position-1);
                return DetailTipFragment.getInstance(BundleBuilder.create().with("tip", template).build());
        }
    }

    @Override
    public int getCount() {
        return count+1;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        detailTipFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        detailTipFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return detailTipFragments.get(position);
    }
}
