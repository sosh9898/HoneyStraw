package com.jyoung.honeystraw.ui.tip;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jyoung.honeystraw.R;
import com.jyoung.honeystraw.application.ApplicationController;
import com.jyoung.honeystraw.base.util.SharedPreferencesService;
import com.jyoung.honeystraw.model.CommentList;
import com.jyoung.honeystraw.model.ResultLike;
import com.jyoung.honeystraw.network.NetworkService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jyoung.honeystraw.ui.tip.CustomHideCommentDialog.HIDE_COMMENT;


/**
 * Created by jyoung on 2017. 8. 1..
 */

public class DetailCommentRecyclerAdapter extends RecyclerView.Adapter {
    List<CommentList> commentList;
    public View.OnClickListener onClickListener;
    Context context;
    int coverNum;
    Activity activity;
    public static final int NORMAL_COMMENT = 1300;

    public static final int COMMENT_LIKE_CHECKED = 801;
    public static final int COMMENT_LIKE_UNCHECKED = 801;


    public DetailCommentRecyclerAdapter(List<CommentList> commentList, View.OnClickListener onClickListener, Context context, int coverNum, Activity activity) {
        this.commentList = commentList;
        this.onClickListener = onClickListener;
        this.context = context;
        this.coverNum = coverNum;
        this.activity = activity;
    }

    public void refreshAdapter(List<CommentList> commentList){
        this.commentList = commentList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case NORMAL_COMMENT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_comment_baseitem, null);
                view.setOnClickListener(onClickListener);
                return new CommentViewHolder(view);
            case HIDE_COMMENT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_comment_hideitem, null);
                view.setOnClickListener(onClickListener);
                return new CommentHideViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            if(holder instanceof CommentViewHolder)
            ((CommentViewHolder)holder).bindView(commentList.get(position));
            else if(holder instanceof CommentHideViewHolder){}
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return commentList != null ? commentList.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return commentList.get(position).getCommentType();
    }

    public class CommentHideViewHolder extends RecyclerView.ViewHolder {
        public CommentHideViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder{
        @InjectView(R.id.comment_profile_image)CircleImageView profileImage;
        @InjectView(R.id.comment_profile_nick)TextView nickText;
        @InjectView(R.id.comment_content)TextView contentText;
        @InjectView(R.id.comment_postdate)TextView postdateText;
        @InjectView(R.id.comment_like_item)RelativeLayout commentLikeItem;
        @InjectView(R.id.comment_like_checked)ImageView commentLikeImage;
        @InjectView(R.id.comment_like_text)TextView commentLikeText;
        @InjectView(R.id.delete_comment)ImageView deleteComment;
        @InjectView(R.id.best_comment)TextView bestComment;
        CommentList comment;
        String timeNow;
        int tempLikeState;

        NetworkService service;
        SharedPreferencesService preferencesService;
        CustomHideCommentDialog customHideCommentDialog;

        public CommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            service = ApplicationController.getInstance().getNetworkService();
            preferencesService = new SharedPreferencesService();
            preferencesService.load(context);
        }

        public void bindView(CommentList comment) throws ParseException {
            this.comment =comment;
            if(getAdapterPosition() == 0) {
                if (commentList.size()>4) {
                    if(comment.getLikeNum() != 0) {
                        bestComment.setVisibility(View.VISIBLE);
                        Log.d("????", commentList.size() + "");
                    }
                }
            }
            else if(getAdapterPosition() != 0)
                bestComment.setVisibility(View.GONE);

            if(!comment.getProfileImage().equals(""))
            Glide.with(context).load(comment.getProfileImage()).into(profileImage);
            nickText.setText(comment.getNickname());
            contentText.setText(comment.getContent());
            commentLikeText.setText(comment.getLikeNum()+"");
            tempLikeState = comment.likeState;
            if(comment.getLikeState() == COMMENT_LIKE_CHECKED)
            commentLikeImage.setVisibility(View.VISIBLE);
            else
                commentLikeImage.setVisibility(View.GONE);

            if(comment.getWriter().equals(preferencesService.getPrefStringData("id"))){
                deleteComment.setVisibility(View.VISIBLE);
            }else
                deleteComment.setVisibility(View.GONE);

            String postDate = comment.getPostdate();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = format.parse(postDate);

            postdateText.setText(calculateTime(date).toString());

            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dateNow = Calendar.getInstance().getTime();
            timeNow = dateformat.format(dateNow);
            customHideCommentDialog = new CustomHideCommentDialog(activity, comment.getNum(),getAdapterPosition(),DetailCommentRecyclerAdapter.this, activity, comment);

            deleteComment.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    customHideCommentDialog.show();
                    return false;
                }
            });
        }


        @OnClick(R.id.comment_like_item)
        void onClick(View view){
            Call<ResultLike> getlikeResult = service.likeselector(coverNum, comment.getNum(), preferencesService.getPrefStringData("id"), tempLikeState, timeNow);

            getlikeResult.enqueue(new Callback<ResultLike>() {
                @Override
                public void onResponse(Call<ResultLike> call, Response<ResultLike> response) {
                    if(response.isSuccessful()){
                        if(response.body().message.equals("ok")) {
                            Log.d("sdfsdfsdf", response.body().likeNum+"");
                            commentLikeText.setText(response.body().likeNum + "");

                            tempLikeState = response.body().likeState;
                            if(tempLikeState == COMMENT_LIKE_CHECKED) {
                                commentLikeImage.setVisibility(View.VISIBLE);
                            }
                            else {
                                commentLikeImage.setVisibility(View.GONE);
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResultLike> call, Throwable t) {

                }
            });
        }
    }

    public String calculateTime(Date date)
    {

        long curTime = System.currentTimeMillis();
        long regTime = date.getTime();
        long diffTime = (curTime - regTime) / 1000;

        Log.d("asdf", curTime + "   " + regTime + "     " + diffTime);


        String msg = null;

        if (diffTime < TIME_MAXIMUM.SEC)
        {
            // sec
            msg = "방금 전";
        }
        else if ((diffTime /= TIME_MAXIMUM.SEC) < TIME_MAXIMUM.MIN)
        {
            // min
            System.out.println(diffTime);

            msg = diffTime + "분 전";
        }
        else if ((diffTime /= TIME_MAXIMUM.MIN) < TIME_MAXIMUM.HOUR)
        {
            // hour
            msg = (diffTime ) + "시간 전";
        }
        else if ((diffTime /= TIME_MAXIMUM.HOUR) < TIME_MAXIMUM.DAY)
        {
            // day
            msg = (diffTime ) + "일 전";
        }
        else if ((diffTime /= TIME_MAXIMUM.DAY) < TIME_MAXIMUM.MONTH)
        {
            // day
            msg = (diffTime ) + "달 전";
        }
        else
        {
            msg = (diffTime) + "년 전";
        }

        return msg;
    }

    private static class TIME_MAXIMUM
    {
        public static final int SEC = 60;
        public static final int MIN = 60;
        public static final int HOUR = 24;
        public static final int DAY = 30;
        public static final int MONTH = 12;
    }
}