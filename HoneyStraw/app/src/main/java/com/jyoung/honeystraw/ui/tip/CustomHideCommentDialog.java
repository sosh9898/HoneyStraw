package com.jyoung.honeystraw.ui.tip;

/**
 * Created by jyoung on 2017. 8. 12..
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.jyoung.honeystraw.R;
import com.jyoung.honeystraw.application.ApplicationController;
import com.jyoung.honeystraw.model.CommentList;
import com.jyoung.honeystraw.model.RequestResult;
import com.jyoung.honeystraw.network.NetworkService;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomHideCommentDialog extends Dialog {
    @InjectView(R.id.alert_title)
    TextView alertTitleText;
    @InjectView(R.id.yes_text)
    TextView yesText;
    @InjectView(R.id.no_text)
    TextView noText;

    public static final int HIDE_COMMENT = 106;

    Activity activity;
    NetworkService service;
    int num,position;
    DetailCommentRecyclerAdapter adapter;
    CommentList comment;

    public CustomHideCommentDialog(Context context, int num, int position, RecyclerView.Adapter adapter, Activity activity, CommentList comment) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.num = num;
        this.position = position;
        this.adapter = (DetailCommentRecyclerAdapter) adapter;
        this.activity =activity;
        this.comment = comment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);
        setContentView(R.layout.dialog_hide);
        ButterKnife.inject(this);

        alertTitleText.setText("해당 댓글을 삭제하시겠습니까?");
        service = ApplicationController.getInstance().getNetworkService();

    }

    @OnClick(R.id.yes_text)
    void onYesClick(final View view) {
        Call<RequestResult> deleteComment = service.deleteMyComment(comment.getNum(),comment.getCoverNum());

        deleteComment.enqueue(new Callback<RequestResult>() {
            @Override
            public void onResponse(Call<RequestResult> call, Response<RequestResult> response) {
                if(response.isSuccessful()){
                    if(response.body().message.equals("ok")){
                        CommentList commentList = new CommentList();
                        commentList.setCommentType(HIDE_COMMENT);
                        adapter.commentList.set(position, commentList);
                        adapter.notifyItemChanged(position);
                        CustomHideCommentDialog.this.dismiss();
                        ((DetailTipCommentActivity)activity).setcommenthidenum();
                    }
                }
            }

            @Override
            public void onFailure(Call<RequestResult> call, Throwable t) {

            }
        });


    }

    @OnClick(R.id.no_text)
    void onNoClick(View view) {
        this.dismiss();
    }

}


