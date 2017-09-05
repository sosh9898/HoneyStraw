package com.jyoung.honeystraw.ui.tabs.home;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jyoung.honeystraw.R;
import com.jyoung.honeystraw.model.Cover;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by jyoung on 2017. 8. 1..
 */

public class HomeFullViewHolder extends RecyclerView.ViewHolder {
    @InjectView(R.id.cover_image_full)ImageView coverImage;
    @InjectView(R.id.cover_title_full)TextView coverTitle;

    public HomeFullViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
   }

    public void bindView(Cover cover){
        Glide.with(coverImage.getContext()).load(cover.getCoverImage()).into(coverImage);
        coverTitle.setText(cover.getCoverContent());
    }
}
