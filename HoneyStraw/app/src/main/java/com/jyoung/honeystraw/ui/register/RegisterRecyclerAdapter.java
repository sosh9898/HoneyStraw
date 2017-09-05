package com.jyoung.honeystraw.ui.register;

import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jyoung.honeystraw.R;
import com.jyoung.honeystraw.base.util.ToastMaker;
import com.jyoung.honeystraw.model.RegisterTip;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.jyoung.honeystraw.ui.register.RegisterActivity.REQ_CODE_SELECT_IMAGE;
import static com.jyoung.honeystraw.ui.register.RegisterActivity.REQ_CODE_SELECT_MORE_IMAGE;
import static com.jyoung.honeystraw.ui.tabs.home.HomeTabRecyclerAdapter.TYPE_BASE;
import static com.jyoung.honeystraw.ui.tabs.home.HomeTabRecyclerAdapter.TYPE_FOOTER;
import static com.jyoung.honeystraw.ui.tabs.home.HomeTabRecyclerAdapter.TYPE_HEADER;

/**
 * Created by jyoung on 2017. 8. 5..
 */

public class RegisterRecyclerAdapter extends RecyclerView.Adapter {
    List<RegisterTip> registerTipList;
    View.OnClickListener onClickListener;
    FragmentActivity tempActivity;
    Context context;


    public RegisterRecyclerAdapter(List<RegisterTip> registerTipList, View.OnClickListener onClickListener, FragmentActivity tempActivity) {
        this.registerTipList = registerTipList;
        this.onClickListener = onClickListener;
        this.tempActivity = tempActivity;
    }

    public void refreshAdapter(List<RegisterTip> registerTipList) {
        this.registerTipList = registerTipList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_HEADER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_register_headeritem, parent, false);
                view.setOnClickListener(onClickListener);
                return new RegisterHeaderViewHolder(view);
            case TYPE_BASE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_register_baseitem, parent, false);
                view.setOnClickListener(onClickListener);
                return new RegisterBaseViewHolder(view);
            case TYPE_FOOTER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_register_footeritem, parent, false);
                view.setOnClickListener(onClickListener);
                return new RegisterFooterViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RegisterHeaderViewHolder) {
            ((RegisterHeaderViewHolder) holder).bindView();

        } else if (holder instanceof RegisterBaseViewHolder) {
            ((RegisterBaseViewHolder) holder).bindView(registerTipList.get(position), position);
        } else {
            ((RegisterFooterViewHolder) holder).bindView();

        }

    }

    @Override
    public int getItemViewType(int position) {
        return registerTipList.get(position).getViewType();
    }

    @Override
    public int getItemCount() {
        return registerTipList != null ? registerTipList.size() : 0;
    }

    public class RegisterHeaderViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.register_header_item)
        RelativeLayout headerItem;
        @InjectView(R.id.register_header_image)
        ImageView headerImage;

        public RegisterHeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }

        public void bindView() {
            headerImage.setImageResource(R.drawable.photo_camera);
        }

        @OnClick(R.id.register_header_item)
        void onClick(View view) {
            Fragment fragment = tempActivity.getSupportFragmentManager().findFragmentById(R.id.register_content);
           if( ((RegisterFragment)fragment).returnImageUrl()<2) {
               Snackbar.make(tempActivity.getWindow().getDecorView().getRootView(), "커버와 배경을 먼저 선택해주세요!!",Snackbar.LENGTH_SHORT).show();
               ((RegisterFragment)fragment).backgroundFocusing();
               return;
           }
           else{
               Intent intent = new Intent(Intent.ACTION_PICK);
               intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
               intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
               intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
               tempActivity.startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
               ToastMaker.makeLongToast(tempActivity, "사진은 최대 10장입니다.");
           }
        }


    }

    public class RegisterBaseViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.register_base_image)
        ImageView baseImage;
        @InjectView(R.id.register_base_number)
        TextView baseNum;

        public RegisterBaseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }

        public void bindView(RegisterTip registerTip, int position) {
            Glide.with(baseImage.getContext()).load(registerTip.getTipImage()).into(baseImage);
            baseNum.setText((position + 1) + "");
        }
    }

    public class RegisterFooterViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.register_footer_item)
        RelativeLayout footerItem;
        @InjectView(R.id.register_footer_image)
        ImageView footerImage;

        public RegisterFooterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }

        public void bindView() {
            footerImage.setImageResource(R.drawable.plus);
        }

        @OnClick(R.id.register_footer_item)
        void onFooterClick(View view) {
            startImagePick();
        }

        public void startImagePick() {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            tempActivity.startActivityForResult(intent, REQ_CODE_SELECT_MORE_IMAGE);
        }
    }
}
