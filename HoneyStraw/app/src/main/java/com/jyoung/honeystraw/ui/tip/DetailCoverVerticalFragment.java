package com.jyoung.honeystraw.ui.tip;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jyoung.honeystraw.R;
import com.jyoung.honeystraw.model.User;
import com.jyoung.honeystraw.ui.mypage.MyPageActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by jyoung on 2017. 8. 4..
 */

public class DetailCoverVerticalFragment extends Fragment {
    @InjectView(R.id.cover_title)TextView coverTitle;
    @InjectView(R.id.cover_mypage_profile_nick)TextView coverNick;
    @InjectView(R.id.cover_mypage_profile_status)TextView statusMessage;
    @InjectView(R.id.cover_mypage_profile_image)CircleImageView coverProfileImage;
    @InjectView(R.id.cover_brand)TextView brandText;
    @InjectView(R.id.cover_postdate)TextView coverPostDate;
    @InjectView(R.id.cover_view_count)TextView coverViewCount;

    User user;

    public DetailCoverVerticalFragment() {
    }

    public static DetailCoverVerticalFragment getInstance(Bundle bundle) {
        DetailCoverVerticalFragment detailCoverFragment = new DetailCoverVerticalFragment();
        detailCoverFragment.setArguments(bundle);
        return detailCoverFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_cover, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = getArguments().getParcelable("tipFirstPage");
        Glide.with(getActivity()).load(user.getProfileImage()).into(coverProfileImage);
        coverTitle.setText(((DetailTipVerticalActivity)getContext()).getCoverTitle());
        coverNick.setText(user.getNickname());
        statusMessage.setText(user.getStateMessage());
        brandText.setText("#"+((DetailTipVerticalActivity)getContext()).getBrandName());
        coverPostDate.setText(((DetailTipVerticalActivity)getContext()).getTempPostdate());
        coverViewCount.setText(((DetailTipVerticalActivity)getContext()).getViewCount()+1+" View");

    }

    @OnClick({R.id.cover_mypage_profile_image, R.id.cover_mypage_profile_nick, R.id.cover_mypage_profile_status})
    void onClickUserInfo(View view){
        Intent intent = new Intent(getContext(), MyPageActivity.class);
        intent.putExtra("userid", user.getId());
        intent.putExtra("nickname", user.getNickname());
        startActivity(intent);
    }


}
