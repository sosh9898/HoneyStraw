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

public class CustomScrollDialog extends Dialog {
    @InjectView(R.id.scroll_left_right_item)LinearLayout scrollLF;
    @InjectView(R.id.scroll_up_down_item)LinearLayout scrollUD;

    @InjectView(R.id.scroll_left_right)
    ImageView scrollLFImage;
    @InjectView(R.id.scroll_up_down)
    ImageView scrollUDImage;

    private View.OnClickListener mLeftClickListener;
    private View.OnClickListener mRightClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dialog_scroll_type);
        ButterKnife.inject(this);

        Glide.with(getContext()).load(R.drawable.scroll_left_right).into(scrollLFImage);
        Glide.with(getContext()).load(R.drawable.scroll_up_down).into(scrollUDImage);

        scrollLF.setOnClickListener(mLeftClickListener);
        scrollUD.setOnClickListener(mRightClickListener);
    }

    public CustomScrollDialog(Context context, View.OnClickListener leftListener,
                             View.OnClickListener rightListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mLeftClickListener = leftListener;
        this.mRightClickListener = rightListener;
    }
}
