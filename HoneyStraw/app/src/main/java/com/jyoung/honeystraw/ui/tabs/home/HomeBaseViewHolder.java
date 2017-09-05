package com.jyoung.honeystraw.ui.tabs.home;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jyoung.honeystraw.R;
import com.jyoung.honeystraw.base.util.SharedPreferencesService;
import com.jyoung.honeystraw.model.Cover;
import com.jyoung.honeystraw.ui.mypage.CustomHideTipDialog;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by jyoung on 2017. 8. 1..
 */

public class HomeBaseViewHolder extends RecyclerView.ViewHolder {
    @InjectView(R.id.half_tip)CardView halfTip;
    @InjectView(R.id.cover_image_half)ImageView coverImage;
    @InjectView(R.id.cover_title_half)TextView coverTitle;
    SharedPreferencesService preferencesService;
    CustomHideTipDialog customHideTipDialog;

    public HomeBaseViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
        preferencesService = new SharedPreferencesService();
        preferencesService.load(itemView.getContext());

   }

    public void bindView(final Cover cover){
        Glide.with(coverImage.getContext()).load(cover.getCoverImage()).into(coverImage);
        coverTitle.setText(cover.getCoverContent());
    }
}
