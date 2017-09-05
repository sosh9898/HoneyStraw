package com.jyoung.honeystraw.ui.tabs.brand;

/**
 * Created by jyoung on 2017. 8. 12..
 */

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.jyoung.honeystraw.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CustomSortTempDialog extends Dialog {
    @InjectView(R.id.recently_sort_text)
    TextView recentlyItem;
    @InjectView(R.id.viewcount_sort_text)
    TextView viewcountItem;
    @InjectView(R.id.straw_sort_text)
    TextView strawItem;
    @InjectView(R.id.comment_sort_text)
    TextView commentItem;

    public View.OnClickListener recentlyListener;
    public View.OnClickListener viewcountListener;
    public View.OnClickListener strawListener;
    public View.OnClickListener commentListener;

    public CustomSortTempDialog(@NonNull Context context, View.OnClickListener recentlyListener, View.OnClickListener viewcountListener, View.OnClickListener strawListener, View.OnClickListener commentListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.recentlyListener = recentlyListener;
        this.viewcountListener = viewcountListener;
        this.strawListener = strawListener;
        this.commentListener = commentListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);
        setContentView(R.layout.dialog_sort_temp);
        ButterKnife.inject(this);

        recentlyItem.setOnClickListener(recentlyListener);
        viewcountItem.setOnClickListener(viewcountListener);
        strawItem.setOnClickListener(strawListener);
        commentItem.setOnClickListener(commentListener);
    }
}
