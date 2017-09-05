package com.jyoung.honeystraw.ui.register;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.jyoung.honeystraw.base.util.BundleBuilder;
import com.jyoung.honeystraw.model.ReturnTips;

import org.parceler.Parcels;

import java.util.List;

/**
 * Created by jyoung on 2017. 8. 6..
 */

public class RegisterTipEditPagerAdapter extends FragmentStatePagerAdapter {
    SparseArray<Fragment> registerTipEditFragments = new SparseArray<Fragment>();
    List<ReturnTips> returnTipsList;
    String[] selectImages;
    int count;
    int flag;
    Parcelable parcelable;

    public RegisterTipEditPagerAdapter(FragmentManager fm, int count, String[] selectImages, int flag) {
        super(fm);
        this.count = count;
        this.selectImages = selectImages;
        this.flag = flag;
    }

    public RegisterTipEditPagerAdapter(FragmentManager fm, int count, List<ReturnTips> returnTipsList, int flag) {
        super(fm);
        this.count = count;
        this.returnTipsList = returnTipsList;
        this.flag = flag;
    }


    @Override
    public Fragment getItem(int position) {
        switch (flag) {
            case 0:
                return RegisterTipEditFragment.getInstance(BundleBuilder.create().with("selectImage", selectImages[position]).build());
            case 1:
                parcelable = Parcels.wrap(returnTipsList.get(position));
                return RegisterTipEditFragment.getInstance(BundleBuilder.create().with("EditMore", parcelable).build());
            case 2:
                parcelable = Parcels.wrap(returnTipsList.get(position));
                return RegisterTipEditFragment.getInstance(BundleBuilder.create().with("EditMore", parcelable).build());
        }
        return null;
    }

    public int getItemPosition(Object object){
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registerTipEditFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registerTipEditFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registerTipEditFragments.get(position);
    }
}
