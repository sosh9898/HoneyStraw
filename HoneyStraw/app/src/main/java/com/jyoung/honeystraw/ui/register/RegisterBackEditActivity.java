package com.jyoung.honeystraw.ui.register;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.chrisbanes.photoview.PhotoView;
import com.jyoung.honeystraw.R;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static android.R.attr.maxHeight;
import static android.R.attr.maxWidth;

public class RegisterBackEditActivity extends AppCompatActivity {
    @InjectView(R.id.register_edit_background_image)
    PhotoView editBackgroundImage;
    String tempUri;
    Uri mDestinationUri;
    int flag = 0;
    int coverType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_back_edit);
        ButterKnife.inject(this);
        getData();
        setLayout();
    }

    public void getData() {
        Intent getData = getIntent();
        if (getData.getExtras().getString("selectBackground") != null) {
            tempUri = getData.getExtras().getString("selectBackground");
        }
        else {
            tempUri = getData.getExtras().getString("selectCover");
            coverType = Integer.parseInt(getData.getExtras().getString("coverType"));
            flag = 1;
        }
        mDestinationUri = Uri.parse(tempUri);


    }

    public void setLayout() {
        editBackgroundImage.setImageBitmap(resizeImage(Uri.parse(tempUri)));
        if(flag == 1) {
            mDestinationUri = Uri.fromFile(new File(getCacheDir(), getImageNameToUri(Uri.parse(tempUri))));

            UCrop.Options options = new UCrop.Options();
            options.setToolbarColor(getResources().getColor(R.color.colorPrimary));
            options.setToolbarTitle("꿀팁 제조기");
            options.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            if (coverType == 103) {
                UCrop.of(Uri.parse(tempUri), mDestinationUri)
                        .withOptions(options)
                        .withAspectRatio(3, 4)
                        .withMaxResultSize(maxWidth, maxHeight)
                        .start(this);
            } else {
                UCrop.of(Uri.parse(tempUri), mDestinationUri)
                        .withOptions(options)
                        .withAspectRatio(3, 2)
                        .withMaxResultSize(maxWidth, maxHeight)
                        .start(this);
            }
        }

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

    public Bitmap resizeImage(
            Uri tempImage) {
        BitmapFactory.Options options = new BitmapFactory.Options();

        InputStream in = null;
        try {
            in = getContentResolver().openInputStream(tempImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);

        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public void updateCropResult(Uri uri) {
        editBackgroundImage.setImageBitmap(resizeImage(uri));
    }

    @OnClick(R.id.back_crop_btn)
    void onClick(View view) {
        mDestinationUri = Uri.fromFile(new File(getCacheDir(), "test.jpeg"));

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

    @OnClick(R.id.back_register_background_commit_btn)
    void onCommitClick(View view) {
        Intent intent = new Intent();
        intent.putExtra("Background", mDestinationUri.toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            tempUri = resultUri.toString();
            updateCropResult(resultUri);
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }
}

