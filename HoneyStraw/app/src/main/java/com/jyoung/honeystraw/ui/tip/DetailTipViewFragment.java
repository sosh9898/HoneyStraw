package com.jyoung.honeystraw.ui.tip;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jyoung.honeystraw.R;
import com.jyoung.honeystraw.base.customview.TipView;
import com.jyoung.honeystraw.model.TipTemplate;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by jyoung on 2017. 9. 14..
 */

public class DetailTipViewFragment extends Fragment {
    @InjectView(R.id.tipview)
    TipView tipView;

    TipTemplate tipTemplate;

    public DetailTipViewFragment() {
    }

    public static DetailTipFragment getInstance(Bundle bundle) {
        DetailTipFragment detailTipFragment = new DetailTipFragment();
        detailTipFragment.setArguments(bundle);
        return detailTipFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_tipview_ver, null);
        ButterKnife.inject(this, view);
        tipTemplate = getArguments().getParcelable("tip");
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTip();
    }

    public void setTip(){
        tipView.setTipImg(tipTemplate.getTipImage());
        tipView.setTipText(tipTemplate.getTipContent());
        tipView.setTempleate(tipTemplate.getTipTemplateNum());

    }


}
