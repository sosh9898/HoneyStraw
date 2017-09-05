package com.jyoung.honeystraw.base;

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

public class BaseViewHolder extends RecyclerView.ViewHolder {
    @InjectView(R.id.cover_image_half)
    ImageView coverImage;
    @InjectView(R.id.cover_title_half)
    TextView coverTitle;

    public BaseViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
    }

    public void bindView(Cover cover) {
        Glide.with(coverImage.getContext()).load(R.drawable.temp_image3).into(coverImage);
//        coverTitle.setText(cover.getCoverContent());
    }
}
