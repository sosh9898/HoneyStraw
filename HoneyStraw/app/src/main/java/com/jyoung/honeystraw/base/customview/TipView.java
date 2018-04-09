package com.jyoung.honeystraw.base.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jyoung.honeystraw.R;


/**
 * Created by jyoung on 2017. 9. 14..
 */

public class TipView extends LinearLayout {

    private final String TAG = "TemplateTipView";

    ImageView tipImg;
    TextView tipText;

    RelativeLayout.LayoutParams paramsText;
    RelativeLayout.LayoutParams paramsImage;


    public TipView(Context context) {
        super(context);
        initView();
    }

    public TipView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
        getAttr(attrs);
    }

    public TipView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        getAttr(attrs, defStyleAttr);
    }


    private void initView() {
        String layoutInfService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(layoutInfService);
        View v = li.inflate(R.layout.tipview, this, false);

        tipImg = (ImageView)v.findViewById(R.id.tipview_image);
        tipText = (TextView) v.findViewById(R.id.tipview_text);
        addView(v);

    }

    private void getAttr(AttributeSet attrs){
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TipView);
        setTypeArray(typedArray);
    }
    private void getAttr(AttributeSet attrs, int defStyle){
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TipView, defStyle, 0);
        setTypeArray(typedArray);

    }

    private void setTypeArray(TypedArray typedArray){

        int img_resID = typedArray.getResourceId(R.styleable.TipView_img, 0);
        String text_string = typedArray.getString(R.styleable.TipView_text);
        tipImg.setBackgroundResource(img_resID);
        tipText.setText(text_string);

        typedArray.recycle();
    }

    public void setTipImg(int img_resID){
        Glide.with(getContext()).load(img_resID).into(tipImg);
    }
    public void setTipImg(String img_url) {
        Glide.with(getContext()).load(img_url).into(tipImg);
    }
    public void setTipText(int text_resID){
        tipText.setText(text_resID);
    }
    public void setTipText(String text){
        tipText.setText(text);
    }

    public void setTempleate(int num){
        switch (num){
            case 1:
                paramsText= new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT, convertDpToPx(200, getContext()));

                paramsText.addRule(RelativeLayout.ABOVE, R.id.tip_image);
                tipText.setLayoutParams(paramsText);
                break;
            case 2:
                paramsText= new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT, convertDpToPx(200, getContext()));                paramsText.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.tip_image);
                tipText.setLayoutParams(paramsText);
                break;
            case 3:
                paramsImage= new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                tipImg.setLayoutParams(paramsImage);

                tipText.setTextSize(20);
                tipText.setGravity(Gravity.CENTER);
                paramsText= new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT, convertDpToPx(200, getContext()));                paramsText.addRule(RelativeLayout.CENTER_IN_PARENT);
                tipText.setLayoutParams(paramsText);
                break;
            default:
                tipImg.setScaleType(ImageView.ScaleType.FIT_XY);
                paramsImage= new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT, convertDpToPx(300, getContext()));
                paramsImage.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                tipImg.setLayoutParams(paramsImage);


                paramsText= new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT, convertDpToPx(200, getContext()));                paramsText = (RelativeLayout.LayoutParams)tipText.getLayoutParams();
                paramsText.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                tipText.setTextSize(16);
                tipText.setGravity(Gravity.BOTTOM);
                tipText.setLayoutParams(paramsText);
                tipText.setPadding(convertDpToPx(13,getContext()),
                        0,convertDpToPx(13,getContext()),convertDpToPx(58, getContext()));



        }

    }

    private int convertDpToPx(float dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }


}
