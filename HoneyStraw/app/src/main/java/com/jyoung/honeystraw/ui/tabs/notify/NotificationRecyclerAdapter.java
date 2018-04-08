package com.jyoung.honeystraw.ui.tabs.notify;

import android.content.Context;
import android.support.v4.app.Fragment;
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
import com.jyoung.honeystraw.model.Notify;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.jyoung.honeystraw.ui.tabs.notify.CustomHideNotifyDialog.HIDE_NOTIFY;


/**
 * Created by jyoung on 2017. 8. 1..
 */

public class NotificationRecyclerAdapter extends RecyclerView.Adapter {
    public View.OnClickListener onClickListener;
    List<Notify> notifyList;
    Fragment fragment;
    Context context;
    CustomHideNotifyDialog customHideNotifyDialog;

    public static final int NOTIFY_TIP_LIKE = 1000;
    public static final int NOTIFY_COMMENT_REGISTER = 1001;
    public static final int NOTIFY_COMMENT_LIKE = 1002;
    public static final int NORMAL_NOTIFY = 330;


    public NotificationRecyclerAdapter(List<Notify> notifyList, View.OnClickListener onClickListener, Fragment fragment, Context context) {
        this.notifyList = notifyList;
        this.onClickListener = onClickListener;
        this.fragment = fragment;
        this.context = context;
    }

    public void refreshAdapter(List<Notify> notifyList) {
        this.notifyList = notifyList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case NORMAL_NOTIFY:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_notification_baseitem, null);
                view.setOnClickListener(onClickListener);
                return new NotificationBaseViewHolder(view);
            case HIDE_NOTIFY:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_notification_hideitem, null);
                view.setOnClickListener(onClickListener);
                return new NotificationHideViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            if(holder instanceof NotificationBaseViewHolder)
            ((NotificationBaseViewHolder) holder).bindView(notifyList.get(position));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return notifyList.get(position).getViewType();
    }

    @Override
    public int getItemCount() {
        return notifyList != null ? notifyList.size() : 0;
    }


    public class NotificationBaseViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.notification_item)
        RelativeLayout notifyItem;
        @InjectView(R.id.notification_delete_item)
        TextView notifyDeleteItem;
        @InjectView(R.id.notify_profile_image)
        CircleImageView notifyProfileImage;
        @InjectView(R.id.notification_target_id)
        TextView targetId;
        @InjectView(R.id.notification_my_id)
        TextView myId;
        @InjectView(R.id.notification_content)
        TextView notifyContent;
        @InjectView(R.id.notification_image)
        ImageView notifyImage;
        @InjectView(R.id.notification_postdate)
        TextView notifyTime;

        public NotificationBaseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }

        public void bindView(final Notify notify) throws ParseException {

            if (notify.getCheck() == 0) {
                notifyItem.setBackgroundColor(fragment.getResources().getColor(R.color.colorNotify));
            } else
                notifyItem.setBackgroundColor(fragment.getResources().getColor(R.color.splashBackground));
            if (notify.getProfileImage() != null)
                Glide.with(fragment).load(notify.getProfileImage()).into(notifyProfileImage);
            targetId.setText(notify.getNickname());
            myId.setText(notify.getTargetId());
            setNotifyType(notify);
            String postDate = notify.getPostdate();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = format.parse(postDate);

            notifyItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    customHideNotifyDialog = new CustomHideNotifyDialog(context, notify.getNum(), getAdapterPosition(), NotificationRecyclerAdapter.this);

                    customHideNotifyDialog.show();
                    return false;
                }
            });

            notifyTime.setText(calculateTime(date).toString());
        }


        public void setNotifyType(Notify notify) {
            switch (notify.getNotifyType()) {
                case NOTIFY_TIP_LIKE:
                    notifyContent.setText("팁을 빨대하였습니다.");
                    Glide.with(notifyImage.getContext()).load(R.drawable.straw_checked).into(notifyImage);
                    break;
                case NOTIFY_COMMENT_REGISTER:
                    notifyContent.setText("팁에 댓글을 남겼습니다.");
                    Glide.with(notifyImage.getContext()).load(R.drawable.comment_notify).into(notifyImage);
                    break;
                case NOTIFY_COMMENT_LIKE:
                    notifyContent.setText("댓글을 좋아합니다.");
                    Glide.with(notifyImage.getContext()).load(R.drawable.like).into(notifyImage);
                    break;
            }
        }
    }

    public static String calculateTime(Date date) {

        long curTime = System.currentTimeMillis();
        long regTime = date.getTime();
        long diffTime = (curTime - regTime) / 1000;

        Log.d("asdf", curTime + "   " + regTime + "     " + diffTime);


        String msg = null;

        if (diffTime < TIME_MAXIMUM.SEC) {
            // sec
            msg = "방금 전";
        } else if ((diffTime /= TIME_MAXIMUM.SEC) < TIME_MAXIMUM.MIN) {
            // min
            System.out.println(diffTime);

            msg = diffTime + "분 전";
        } else if ((diffTime /= TIME_MAXIMUM.MIN) < TIME_MAXIMUM.HOUR) {
            // hour
            msg = (diffTime) + "시간 전";
        } else if ((diffTime /= TIME_MAXIMUM.HOUR) < TIME_MAXIMUM.DAY) {
            // day
            msg = (diffTime) + "일 전";
        } else if ((diffTime /= TIME_MAXIMUM.DAY) < TIME_MAXIMUM.MONTH) {
            // day
            msg = (diffTime) + "달 전";
        } else {
            msg = (diffTime) + "년 전";
        }

        return msg;
    }

    private static class TIME_MAXIMUM {
        public static final int SEC = 60;
        public static final int MIN = 60;
        public static final int HOUR = 24;
        public static final int DAY = 30;
        public static final int MONTH = 12;
    }


    public class NotificationHideViewHolder extends RecyclerView.ViewHolder {
        public NotificationHideViewHolder(View itemView) {
            super(itemView);
        }
    }

}