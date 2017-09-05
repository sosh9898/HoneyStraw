package com.jyoung.honeystraw.ui.tip;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jyoung.honeystraw.R;
import com.jyoung.honeystraw.model.TipTemplate;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by jyoung on 2017. 8. 4..
 */

public class DetailTipFragment extends Fragment {
    @InjectView(R.id.tip_image)ImageView tipImage;
    @InjectView(R.id.tip_text)TextView tipText;
    TipTemplate tipTemplate;

    public DetailTipFragment() {
    }

    public static DetailTipFragment getInstance(Bundle bundle) {
        DetailTipFragment detailTipFragment = new DetailTipFragment();
        detailTipFragment.setArguments(bundle);
        return detailTipFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_tip, null);
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
        Glide.with(getContext()).load(tipTemplate.getTipImage()).into(tipImage);
        tipText.setText(tipTemplate.getTipContent());
    }


}
