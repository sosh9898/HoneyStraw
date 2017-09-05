package com.jyoung.honeystraw.ui.register;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jyoung.honeystraw.R;
import com.jyoung.honeystraw.model.ReturnTips;

import org.parceler.Parcels;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by jyoung on 2017. 8. 6..
 */

public class RegisterTipEditFragment extends Fragment {
    @InjectView(R.id.tip_register_image)
    ImageView tipRegisterImage;
    @InjectView(R.id.tip_register_edit)
    EditText tipResgisterEdit;

    String resultImage;
    String selectImage;
    String resultContent;
    ReturnTips returnTips;
    int flag, styleFlag;

    public RegisterTipEditFragment() {
    }

    public static RegisterTipEditFragment getInstance(Bundle bundle) {
        RegisterTipEditFragment registerTipEditFragment = new RegisterTipEditFragment();
        registerTipEditFragment.setArguments(bundle);
        return registerTipEditFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_tip_template, null);
        ButterKnife.inject(this, view);
        if (getArguments().getString("selectImage") != null) {
            selectImage = getArguments().getString("selectImage");
            resultImage = selectImage;
        } else {
            flag = 1;
            Parcelable parcelable = getArguments().getParcelable("EditMore");
            returnTips = Parcels.unwrap(parcelable);
            resultImage = returnTips.getReturnImageUri();
            styleFlag = returnTips.getReturnStyleFlag();
            resultContent = returnTips.getReturnContent();
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTip();
    }

    public String getContent() {
        return tipResgisterEdit.getText().toString();
    }

    public String getImageUri() {
        return resultImage;
    }

    public int getStyleFlag() {
        return styleFlag;
    }

    public void setImageUri(Uri uri) {
        Glide.with(getContext()).load(uri).into(tipRegisterImage);
        resultImage = uri.toString();
    }

    public void setTip() {
        if (flag == 1) {
            tipResgisterEdit.setText(returnTips.getReturnContent());
            Glide.with(getContext()).load(returnTips.getReturnImageUri()).into(tipRegisterImage);
            resultContent = tipResgisterEdit.getText().toString();

        } else
            Glide.with(getContext()).load(selectImage).into(tipRegisterImage);

    }


    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

}




