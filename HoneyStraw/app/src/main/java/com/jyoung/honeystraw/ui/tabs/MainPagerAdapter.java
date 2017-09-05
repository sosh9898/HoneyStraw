package com.jyoung.honeystraw.ui.tabs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.jyoung.honeystraw.ui.tabs.brand.BrandTabFragment;
import com.jyoung.honeystraw.ui.tabs.home.HomeTabFragment;
import com.jyoung.honeystraw.ui.tabs.notify.NotificationFragment;
import com.jyoung.honeystraw.ui.tabs.straw.StrawTabFragment;

/**
 * Created by jyoung on 2017. 8. 1..
 */

public class MainPagerAdapter extends FragmentStatePagerAdapter {
    SparseArray<Fragment> mainPager = new SparseArray<Fragment>();

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return HomeTabFragment.getInstance();
            case 1:
                return BrandTabFragment.getInstance();
            case 2:
                return StrawTabFragment.getInstance();
            case 3:
                return NotificationFragment.getInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        mainPager.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        mainPager.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return mainPager.get(position);
    }
}
