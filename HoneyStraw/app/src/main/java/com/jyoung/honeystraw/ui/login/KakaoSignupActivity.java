package com.jyoung.honeystraw.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.jyoung.honeystraw.application.ApplicationController;
import com.jyoung.honeystraw.base.util.SharedPreferencesService;
import com.jyoung.honeystraw.model.CheckId;
import com.jyoung.honeystraw.network.NetworkService;
import com.jyoung.honeystraw.ui.signup.SignUpActivity;
import com.jyoung.honeystraw.ui.tabs.TabActivity;
import com.kakao.auth.ErrorCode;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jyoung on 2017. 8. 16..
 */

public class KakaoSignupActivity extends Activity {
    SharedPreferencesService preferencesService;
    NetworkService service;

    int flag =0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferencesService = new SharedPreferencesService();
        preferencesService.load(getApplicationContext());
        service = ApplicationController.getInstance().getNetworkService();

        requestMe();
    }

    protected void requestMe(){
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user inof. msg =" + errorResult;
                Log.v("fail", message);

                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorMessage());
                if(result == ErrorCode.CLIENT_ERROR_CODE){
                    finish();
                }else {
                    redirectLoginActivity();
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {

                redirectLoginActivity();
            }

            @Override
            public void onNotSignedUp() {

                showSignup();
            }

            @Override
            public void onSuccess(final UserProfile result) {


                final Call<CheckId> getCheckId = service.getCheckId(result.getEmail());

                getCheckId.enqueue(new Callback<CheckId>() {
                    @Override
                    public void onResponse(Call<CheckId> call, Response<CheckId> response) {
                        if(response.isSuccessful()){
                            if(response.body().checkId == 0){
                                redirectMainActivity(result.getProfileImagePath().toString(), result.getEmail());
                            }
                            else {
                                preferencesService.setPrefData("id", result.getEmail());
                                Intent intent = new Intent(getApplicationContext(), TabActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CheckId> call, Throwable t) {

                    }
                });

            }
        });
    }

    protected  void showSignup(){
        Log.d("kakaokkkk", "?????");

        redirectLoginActivity();
    }

    public void redirectMainActivity(String profileImage, String kakaoId){
        Intent intent = new Intent(this, SignUpActivity.class);
        intent.putExtra("profileImage", profileImage);
        intent.putExtra("id", kakaoId);
        startActivity(intent);
        finish();
    }

    protected  void redirectLoginActivity(){
        final Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }
}
