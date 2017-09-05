package com.jyoung.honeystraw.ui.signup;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.jyoung.honeystraw.R;
import com.jyoung.honeystraw.application.ApplicationController;
import com.jyoung.honeystraw.base.util.SharedPreferencesService;
import com.jyoung.honeystraw.model.RequestResult;
import com.jyoung.honeystraw.model.User;
import com.jyoung.honeystraw.network.NetworkService;
import com.jyoung.honeystraw.ui.tabs.TabActivity;

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

import static com.jyoung.honeystraw.ui.mypage.MyPageEditActivity.REQ_CODE_SELECT_PROFILE;

public class SignUpActivity extends AppCompatActivity {
    NetworkService service;
    @InjectView(R.id.sign_up_image)CircleImageView singupImage;
    @InjectView(R.id.sign_up_nickname)EditText singupNickname;
    @InjectView(R.id.state_message_edit)EditText stateMessageEdit;
    String tempImage;
    String tempEmail;
    SharedPreferencesService preferencesService;
    Uri uri;
    String imgUrl = "";
    int flag =0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.inject(this);
        service = ApplicationController.getInstance().getNetworkService();
        preferencesService = new SharedPreferencesService();
        preferencesService.load(getApplicationContext());
        getData();
        setLayout();
    }

    public void getData(){
        Intent intent = getIntent();
        tempImage = intent.getExtras().getString("profileImage");
        imgUrl = tempImage;
        tempEmail = intent.getExtras().getString("id");
    }

    public void setLayout(){
        if(tempImage != null)
        Glide.with(getApplicationContext()).load(tempImage).into(singupImage);
    }

    @OnClick(R.id.sign_up_btn)
    void onClick(View view){
        User user = new User();
        user.id = tempEmail;
        user.nickname = singupNickname.getText().toString();
        user.stateMessage = stateMessageEdit.getText().toString();
        if(flag ==0){
            user.profileImage = tempImage;
            Call<RequestResult> signupnodeit = service.registerNoEditUser(user);

            signupnodeit.enqueue(new Callback<RequestResult>() {
                @Override
                public void onResponse(Call<RequestResult> call, Response<RequestResult> response) {
                    if(response.isSuccessful()){
                        if(response.body().message.equals("ok")){
                            preferencesService.setPrefData("id", tempEmail);
                            Intent intent = new Intent(getApplicationContext(), TabActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                }

                @Override
                public void onFailure(Call<RequestResult> call, Throwable t) {

                }
            });
        }
        else if(flag ==1) {
            RequestBody id = RequestBody.create(MediaType.parse("multipart/form-data"), user.getId());
            RequestBody nickname = RequestBody.create(MediaType.parse("multipart/form-data"), user.getNickname());
            RequestBody stateMessage = RequestBody.create(MediaType.parse("multipart/form-data"), user.getStateMessage());

            MultipartBody.Part profileImage;

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

            Call<RequestResult> signup = service.registerUser(profileImage, id, nickname,stateMessage);

            signup.enqueue(new Callback<RequestResult>() {
                @Override
                public void onResponse(Call<RequestResult> call, Response<RequestResult> response) {
                    if (response.isSuccessful()) {
                        if (response.body().message.equals("ok")) {
                            preferencesService.setPrefData("id", tempEmail);
                            Intent intent = new Intent(getApplicationContext(), TabActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                }

                @Override
                public void onFailure(Call<RequestResult> call, Throwable t) {
                    Log.d("sdfsdf", t.toString());
                }
            });
        }
    }
    @OnClick(R.id.sign_up_edit_image)
    void onClickEditProfile(View view){
        flag =1;
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQ_CODE_SELECT_PROFILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == REQ_CODE_SELECT_PROFILE){
                uri = data.getData();
                imgUrl = uri.toString();
                Glide.with(getApplicationContext()).load(uri).into(singupImage);
            }
        }
    }
}
