package com.jyoung.honeystraw.ui.mypage;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jyoung.honeystraw.R;
import com.jyoung.honeystraw.base.util.SharedPreferencesService;
import com.jyoung.honeystraw.model.Cover;
import com.jyoung.honeystraw.model.User;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.jyoung.honeystraw.ui.mypage.CustomHideTipDialog.HIDE_TIP;
import static com.jyoung.honeystraw.ui.tabs.home.HomeTabRecyclerAdapter.TYPE_BASE;
import static com.jyoung.honeystraw.ui.tabs.home.HomeTabRecyclerAdapter.TYPE_FULL;
import static com.jyoung.honeystraw.ui.tabs.home.HomeTabRecyclerAdapter.TYPE_HEADER;


/**
 * Created by jyoung on 2017. 8. 1..
 */

public class MyPageRecyclerAdapter extends RecyclerView.Adapter {
    public List<Cover> coverList;
    public View.OnClickListener clickListener;
    public Context context;
    public User user;

    public MyPageRecyclerAdapter(List<Cover> coverList, View.OnClickListener clickListener, Context context) {
        this.coverList = coverList;
        this.clickListener = clickListener;
        this.context = context;
    }

    public void refreshAdapter(User user, List<Cover> coverList){
        this.user =user;
        this.coverList = coverList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case TYPE_HEADER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_mypage_header, parent, false);
                view.setOnClickListener(clickListener);
                return new MyPageHeaderViewHolder(view);
            case TYPE_BASE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_home_baseitem, parent, false);
                view.setOnClickListener(clickListener);
                return new MypageBaseViewHolder(view);
            case TYPE_FULL:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_home_baseitem_full, parent, false);
                view.setOnClickListener(clickListener);
                return new MypageFullViewHolder(view);
            case HIDE_TIP:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_home_hideitem, parent, false);
                view.setOnClickListener(clickListener);
                return new TipHideViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MypageBaseViewHolder){
            ((MypageBaseViewHolder)holder).bindView(coverList.get(position));

            holder.getAdapterPosition();  ///FIXME 여기 이런식으로 가능!!
        }else if(holder instanceof MyPageHeaderViewHolder){
            ((MyPageHeaderViewHolder)holder).bindView();
        }
        else if(holder instanceof  MypageFullViewHolder){
            ((MypageFullViewHolder)holder).bindView(coverList.get(position));
        }
        else{}
    }

    @Override
    public int getItemViewType(int position) {
        return coverList.get(position).getCoverType();
    }

    @Override
    public int getItemCount() {
        return coverList != null ? coverList.size() : 0;
    }




    public class MyPageHeaderViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.profile_image)CircleImageView profileImage;
        @InjectView(R.id.profile_nickname)EditText nickNameText;
        @InjectView(R.id.status_message)EditText statusMessageEdit;
        @InjectView(R.id.nickname_tip)TextView nickNameItem;
        @InjectView(R.id.profile_like_num)TextView likeCount;
        @InjectView(R.id.profile_straw_num)TextView strawCount;
        @InjectView(R.id.profile_tip_num)TextView tipCount;

        public MyPageHeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }

        public void bindView(){
            likeCount.setText(user.getLikeCount()+"");
            strawCount.setText(user.getStrawCount()+"");
            nickNameText.setText(user.getNickname());
            nickNameItem.setText(user.getNickname());
            tipCount.setText(user.getTipCount()+"");
            statusMessageEdit.setText(user.getStateMessage());
            if(user.getProfileImage() != null) {
                Glide.with(profileImage.getContext()).load(user.getProfileImage()).into(profileImage);
            }
        }
    }

    public class TipHideViewHolder extends RecyclerView.ViewHolder {
        public TipHideViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class MypageBaseViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.half_tip)CardView halfTip;
        @InjectView(R.id.cover_image_half)ImageView coverImage;
        @InjectView(R.id.cover_title_half)TextView coverTitle;
        SharedPreferencesService preferencesService;
        CustomHideTipDialog customHideTipDialog;

        public MypageBaseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            preferencesService = new SharedPreferencesService();
            preferencesService.load(itemView.getContext());

        }

        public void bindView(final Cover cover){
            Glide.with(coverImage.getContext()).load(cover.getCoverImage()).into(coverImage);
            coverTitle.setText(cover.getCoverContent());
            customHideTipDialog = new CustomHideTipDialog(context,cover.getCoverNum(),getAdapterPosition(),MyPageRecyclerAdapter.this);

            halfTip.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(cover.getUserId().equals(preferencesService.getPrefStringData("id"))) {
                        customHideTipDialog.show();
                        return false;
                    }else
                        return true;
                }
            });
        }


    }

    public class MypageFullViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.cover_image_full)ImageView coverImage;
        @InjectView(R.id.cover_title_full)TextView coverTitle;
        @InjectView(R.id.full_tip)CardView fullTip;
        SharedPreferencesService preferencesService;
        CustomHideTipDialog customHideTipDialog;
        public MypageFullViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            preferencesService = new SharedPreferencesService();
            preferencesService.load(itemView.getContext());
        }

        public void bindView(final Cover cover){
            Glide.with(coverImage.getContext()).load(cover.getCoverImage()).into(coverImage);
            coverTitle.setText(cover.getCoverContent());
            customHideTipDialog = new CustomHideTipDialog(context,cover.getCoverNum(),getAdapterPosition(),MyPageRecyclerAdapter.this);

            notifyDataSetChanged();
            fullTip.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(cover.getUserId().equals(preferencesService.getPrefStringData("id"))) {
                        customHideTipDialog.show();
                        return false;
                    }else
                        return true;
                }
            });
        }
    }

}
