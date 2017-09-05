package com.jyoung.honeystraw.ui.tip;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.jyoung.honeystraw.R;
import com.jyoung.honeystraw.application.ApplicationController;
import com.jyoung.honeystraw.base.util.SharedPreferencesService;
import com.jyoung.honeystraw.model.Comment;
import com.jyoung.honeystraw.model.CommentList;
import com.jyoung.honeystraw.model.CommentResult;
import com.jyoung.honeystraw.model.RequestResult;
import com.jyoung.honeystraw.network.NetworkService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jyoung.honeystraw.ui.tip.DetailTipActivity.REQ_COMMENT_BACK;

public class DetailTipCommentActivity extends AppCompatActivity {
    @InjectView(R.id.comment_toolbar)Toolbar commentToolbar;
    @InjectView(R.id.comment_rcv)RecyclerView commentRecycler;
    @InjectView(R.id.comment_edit)EditText commentEdit;
    @InjectView(R.id.comment_register_image)Button commentImage;

    SharedPreferencesService preferencesService;

    List<CommentList> commentList;
    DetailCommentRecyclerAdapter detailCommentRecyclerAdapter;
    int tempNum;
    NetworkService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tip_comment);
        ButterKnife.inject(this);
        preferencesService = new SharedPreferencesService();
        preferencesService.load(getApplicationContext());
        service = ApplicationController.getInstance().getNetworkService();
        Intent intent = getIntent();
        tempNum = intent.getExtras().getInt("coverNum");
        commentEdit.requestFocus();
        setToolbar();
        setRecycler();
        networking();



    }

    public void networking(){

        Call<CommentResult> getCommentList = service.getCommentList(tempNum, preferencesService.getPrefStringData("id"));

        getCommentList.enqueue(new Callback<CommentResult>() {
            @Override
            public void onResponse(Call<CommentResult> call, Response<CommentResult> response) {
                if(response.isSuccessful()){
                    commentList = response.body().result.commentList;
                    detailCommentRecyclerAdapter.refreshAdapter(commentList);
                    setcommentnum();
                }
            }

            @Override
            public void onFailure(Call<CommentResult> call, Throwable t) {
                Log.d("fdfdf", t.toString());
            }
        });


    }

    public void setcommentnum(){
        commentToolbar.setTitle("댓글 "+commentList.size()+"개");

    }
    public void setcommenthidenum(){
        commentToolbar.setTitle("댓글 "+(commentList.size()-1)+"개");

    }

    public void setRecycler(){
        commentList = new ArrayList<>();
        detailCommentRecyclerAdapter = new DetailCommentRecyclerAdapter(commentList, onClickListener, getApplicationContext(), tempNum, this);
        commentRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        commentRecycler.setAdapter(detailCommentRecyclerAdapter);
        commentRecycler.setHasFixedSize(true);

    }
    public void setToolbar() {
        commentToolbar.setTitle("댓글 0개");
        commentToolbar.setTitleTextColor(getResources().getColor(R.color.splashBackground));
        commentToolbar.setNavigationIcon(R.drawable.left_arrow);
        commentToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("commentCount", commentList.size());
                setResult(REQ_COMMENT_BACK, intent);
                finish();
            }
        });
    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    @OnClick(R.id.comment_register_image)
    void onClickCommentRegister(View view){
        if(!commentEdit.getText().equals("")) {
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = Calendar.getInstance().getTime();
            String postdate = dateformat.format(date);


            Comment comment = new Comment();
            comment.content = commentEdit.getText().toString();
            comment.coverNum = tempNum;
            comment.writer = preferencesService.getPrefStringData("id");
            comment.postdate = postdate;

            final Call<RequestResult> registerComment = service.registerComment(tempNum, comment);

            registerComment.enqueue(new Callback<RequestResult>() {
                @Override
                public void onResponse(Call<RequestResult> call, Response<RequestResult> response) {
                    if (response.isSuccessful()) {
                        if(response.body().message.equals("ok")) {
                            networking();
                            commentEdit.setText("");
                            hideSoftKeyboard(DetailTipCommentActivity.this);
                            commentRecycler.scrollToPosition(commentRecycler.getAdapter().getItemCount()-1);
                        }

                    }
                }

                @Override
                public void onFailure(Call<RequestResult> call, Throwable t) {

                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("commentCount", commentList.size());
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }
    public void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }
}

