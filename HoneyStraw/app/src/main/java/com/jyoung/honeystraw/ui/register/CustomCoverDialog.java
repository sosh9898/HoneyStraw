package com.jyoung.honeystraw.ui.register;

/**
 * Created by jyoung on 2017. 8. 12..
 */

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.jyoung.honeystraw.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CustomCoverDialog extends Dialog {
    @InjectView(R.id.cover_type_full_item)
    LinearLayout coverFull;
    @InjectView(R.id.cover_type_half_item)
    LinearLayout coverHalf;
    @InjectView(R.id.cover_full)
    ImageView fullTypeImage;
    @InjectView(R.id.cover_half)
    ImageView halfTypeImage;

    private View.OnClickListener mLeftClickListener;
    private View.OnClickListener mRightClickListener;

    public CustomCoverDialog(Context context,
                             View.OnClickListener leftListener,
                             View.OnClickListener rightListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mLeftClickListener = leftListener;
        this.mRightClickListener = rightListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);
        setContentView(R.layout.dialog_cover_type);
        ButterKnife.inject(this);

        Glide.with(getContext()).load(R.drawable.cover_type_full).into(fullTypeImage);
        Glide.with(getContext()).load(R.drawable.cover_type_falf).into(halfTypeImage);

        coverFull.setOnClickListener(mLeftClickListener);
        coverHalf.setOnClickListener(mRightClickListener);
    }

}
