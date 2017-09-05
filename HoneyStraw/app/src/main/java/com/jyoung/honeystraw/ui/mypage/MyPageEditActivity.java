package com.jyoung.honeystraw.ui.mypage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jyoung.honeystraw.R;
import com.jyoung.honeystraw.application.ApplicationController;
import com.jyoung.honeystraw.base.util.SharedPreferencesService;
import com.jyoung.honeystraw.model.RequestResult;
import com.jyoung.honeystraw.model.User;
import com.jyoung.honeystraw.network.NetworkService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPageEditActivity extends AppCompatActivity {
    @InjectView(R.id.mypage_edit_toolbar)Toolbar myPageEditToolbar;
    @InjectView(R.id.profile_image_edit)CircleImageView editProfileImage;
    @InjectView(R.id.profile_nickname_edit)EditText editNickname;
    @InjectView(R.id.status_message_edit)EditText editStatusMessage;
    @InjectView(R.id.mypage_edit_save_text)TextView saveText;

    public static final int REQ_CODE_SELECT_PROFILE = 600;
    SharedPreferencesService preferencesService;
    NetworkService service;
    Uri uri;
    User user;
    String imgUrl;

    int flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page_edit);
        ButterKnife.inject(this);
        preferencesService = new SharedPreferencesService();
        preferencesService.load(getApplicationContext());
        service = ApplicationController.getInstance().getNetworkService();
        setToolbar();
        networkingBefore();

    }

    public void setToolbar(){
        myPageEditToolbar.setTitle("정보 수정");
        myPageEditToolbar.setTitleTextColor(getResources().getColor(R.color.splashBackground));
        setSupportActionBar(myPageEditToolbar);

        myPageEditToolbar.setNavigationIcon(R.drawable.left_arrow);
        myPageEditToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void networkingBefore(){
        Call<User> getUserInfo = service.getProfile(preferencesService.getPrefStringData("id"));

        getUserInfo.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    user = response.body();
                    if(user.getProfileImage() != null)
                        Glide.with(editProfileImage.getContext()).load(user.getProfileImage()).into(editProfileImage);
                    editNickname.setText(user.getNickname());
                    editStatusMessage.setText(user.getStateMessage());
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    public void networkingAfter(){
        User user = new User();
        user.setProfileImage(user.profileImage);
        user.nickname = editNickname.getText().toString();
        user.stateMessage = editStatusMessage.getText().toString();
        user.id = preferencesService.getPrefStringData("id");
        if(flag == 0){
            // 변경 x

            Call<RequestResult> editProfile = service.editProfile(user);

            editProfile.enqueue(new Callback<RequestResult>() {
                @Override
                public void onResponse(Call<RequestResult> call, Response<RequestResult> response) {
                    if(response.isSuccessful()){
                        if(response.body().message.equals("ok")){
                            finish();
                        }
                    }
                }

                @Override
                public void onFailure(Call<RequestResult> call, Throwable t) {

                }
            });
        }
        else if(flag ==1) { // 변경 o
            RequestBody id = RequestBody.create(MediaType.parse("multipart/form-data"), user.id);
            RequestBody nickname = RequestBody.create(MediaType.parse("multipart/form-data"), user.nickname);
            RequestBody state_message = RequestBody.create(MediaType.parse("multipart/form-data"), user.stateMessage);

            MultipartBody.Part profileImage;
            imgUrl = uri.toString();
            if (imgUrl == "") {
                profileImage = null;
            } else {

                BitmapFactory.Options options = new BitmapFactory.Options();

                InputStream in = null;
                try {
                    in = getContentResolver().openInputStream(Uri.parse(imgUrl));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                RequestBody photoBody = RequestBody.create(MediaType.parse("image/jpg"), baos.toByteArray());

                File photo = new File(imgUrl);
                profileImage = MultipartBody.Part.createFormData("profile_image", photo.getName(), photoBody);
            }


            Call<RequestResult> editMypageWithProfile = service.editProfileWithImage(profileImage, id, nickname, state_message);

            editMypageWithProfile.enqueue(new Callback<RequestResult>() {
                @Override
                public void onResponse(Call<RequestResult> call, Response<RequestResult> response) {
                    if (response.isSuccessful()) {
                        if (response.body().message.equals("ok")) {
                            finish();
                        }
                    }
                }

                @Override
                public void onFailure(Call<RequestResult> call, Throwable t) {

                }
            });
        }
    }

    @OnClick(R.id.profile_image_edit)
    void onClickImageEdit(View view){
        flag=1;
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQ_CODE_SELECT_PROFILE);
    }

    @OnClick(R.id.mypage_edit_save_text)
    void onClick(View view){
        networkingAfter();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if (requestCode == REQ_CODE_SELECT_PROFILE){

                uri = data.getData();
                if(uri.toString() != null)
                    Glide.with(editProfileImage.getContext()).load(uri.toString()).into(editProfileImage);
            }
        }
    }
}
