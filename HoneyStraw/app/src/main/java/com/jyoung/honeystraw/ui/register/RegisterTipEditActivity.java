package com.jyoung.honeystraw.ui.register;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.jyoung.honeystraw.R;
import com.jyoung.honeystraw.model.ReturnTips;
import com.yalantis.ucrop.UCrop;

import org.parceler.Parcels;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static android.R.attr.maxHeight;
import static android.R.attr.maxWidth;

public class RegisterTipEditActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    @InjectView(R.id.tip_register_edit_viewpager)
    ViewPager tipviewPager;
    @InjectView(R.id.page_text)
    TextView pageNumText;
    @InjectView(R.id.commit_btn)TextView commitBtn;
    RegisterTipEditPagerAdapter tipviewPagerAdapter;
    String[] selectImages;
    String[] contentList;
    int position, flag, arrayLength;
    List<ReturnTips> returnTipsList;
    Uri mDestinationUri;
    Parcelable editMore;


    static final int REQ_IMAGE_CROP = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_tip_edit);
        ButterKnife.inject(this);
        getData();
        setLayout();


    }

    public void getData() {
        Intent getData = getIntent();
        flag = getData.getExtras().getInt("flag");
        switch (flag) {
            case 0:
                selectImages = new String[getData.getStringArrayExtra("selectImages").length];
                selectImages = getData.getStringArrayExtra("selectImages");
                arrayLength = selectImages.length;
                break;
            case 1:
                editMore = getData.getParcelableExtra("EditMore");
                returnTipsList = Parcels.unwrap(editMore);
                position = getData.getExtras().getInt("position");
                arrayLength = returnTipsList.size();
                break;
            case 2:
                editMore = getData.getParcelableExtra("EditMore");
                returnTipsList = Parcels.unwrap(editMore);
                position = returnTipsList.size()-1;
                arrayLength = returnTipsList.size();
        }
    }

    public void setLayout() {

        switch (flag) {
            case 0:
                tipviewPagerAdapter = new RegisterTipEditPagerAdapter(getSupportFragmentManager(), selectImages.length, selectImages, flag);
                tipviewPager.setAdapter(tipviewPagerAdapter);
                tipviewPager.setCurrentItem(0);
                tipviewPager.setOffscreenPageLimit(selectImages.length);
                tipviewPager.addOnPageChangeListener(this);
                break;
            case 1:
                tipviewPagerAdapter = new RegisterTipEditPagerAdapter(getSupportFragmentManager(), returnTipsList.size(), returnTipsList, flag);
                tipviewPager.setAdapter(tipviewPagerAdapter);
                tipviewPager.setCurrentItem(position);
                tipviewPager.setOffscreenPageLimit(returnTipsList.size());
                tipviewPager.addOnPageChangeListener(this);
                break;
            case 2:
                tipviewPagerAdapter = new RegisterTipEditPagerAdapter(getSupportFragmentManager(), returnTipsList.size(), returnTipsList, flag);
                tipviewPager.setAdapter(tipviewPagerAdapter);
                tipviewPager.setCurrentItem(position);
                tipviewPager.setOffscreenPageLimit(returnTipsList.size());
                tipviewPager.addOnPageChangeListener(this);
                break;
        }
    }

    final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            handler.post(update);
        }
    };

    final Runnable update = new Runnable() {
        public void run() {
            pageNumText.setText(((tipviewPager.getCurrentItem()+1)+"/"+tipviewPagerAdapter.getCount()));
        }
    };


    @OnClick(R.id.commit_btn)
    void commitOnClick(View view) {
            String content, imageUri;
            int returnStyle;
            returnTipsList = new ArrayList<>();
            for (int i = 0; i < arrayLength; i++) {
                Fragment fragment = tipviewPagerAdapter.getRegisteredFragment(i);
                content = ((RegisterTipEditFragment) fragment).getContent();
                imageUri = ((RegisterTipEditFragment) fragment).getImageUri();
                returnStyle = ((RegisterTipEditFragment) fragment).getStyleFlag();
                returnTipsList.add(new ReturnTips(imageUri, content, returnStyle));
            }

            Intent intent = new Intent();
            Parcelable tips = Parcels.wrap(returnTipsList);
            intent.putExtra("returnTips", tips);
            setResult(RESULT_OK, intent);
            finish();
    }



    @OnClick(R.id.image_crop_btn)
    void cropOnClick(View view) {
        Fragment fragment = tipviewPagerAdapter.getRegisteredFragment(tipviewPager.getCurrentItem());
        String tempUri = ((RegisterTipEditFragment) fragment).getImageUri();
        mDestinationUri = Uri.fromFile(new File(getCacheDir(), getImageNameToUri(Uri.parse(tempUri))));

        UCrop.Options options = new UCrop.Options();
        options.setToolbarColor(getResources().getColor(R.color.colorPrimary));
        options.setToolbarTitle("꿀팁 제조기");
        options.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        UCrop.of(Uri.parse(tempUri), mDestinationUri)
                .withOptions(options)
                .withAspectRatio(0, 0)
                .withMaxResultSize(maxWidth, maxHeight)
                .start(this);
    }

    public void updateCropResult(Uri uri){
        Fragment fragment = tipviewPagerAdapter.getRegisteredFragment(tipviewPager.getCurrentItem());
        int temp = tipviewPager.getCurrentItem();
        ((RegisterTipEditFragment)fragment).setImageUri(uri);
        tipviewPager.setCurrentItem(temp);
    }

    public String getImageNameToUri(Uri data) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        String imgPath = cursor.getString(column_index);
        String imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1);

        return imgName;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            updateCropResult(resultUri);
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

    @OnClick(R.id.register_edit_back_btn)
    void onClick(View view){
        finish();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        handler.sendEmptyMessage(tipviewPager.getCurrentItem());
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}

