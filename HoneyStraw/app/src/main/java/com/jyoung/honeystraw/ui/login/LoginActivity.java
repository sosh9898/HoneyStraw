package com.jyoung.honeystraw.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.jyoung.honeystraw.R;
import com.jyoung.honeystraw.application.ApplicationController;
import com.jyoung.honeystraw.base.util.SharedPreferencesService;
import com.jyoung.honeystraw.model.CheckId;
import com.jyoung.honeystraw.network.NetworkService;
import com.jyoung.honeystraw.ui.signup.SignUpActivity;
import com.jyoung.honeystraw.ui.tabs.TabActivity;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.util.exception.KakaoException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jyoung on 2017. 8. 16..
 */

public class LoginActivity extends AppCompatActivity {
    @InjectView(R.id.facebook_login)
    Button loginBtn;
    @InjectView(R.id.login_toolbar)
    Toolbar loginToolbar;

    CallbackManager callbackManager;
    public SessionCallback callback;
    JSONObject json;

    SharedPreferencesService preferencesService;
    NetworkService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        preferencesService = new SharedPreferencesService();
        preferencesService.load(this);

        service = ApplicationController.getInstance().getNetworkService();

        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                Session.getCurrentSession().removeAccessToken();
                Log.d("dfdf", "dd");
            }
        });

        setToolbar();
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
    }


    public void setToolbar() {
        loginToolbar.setNavigationIcon(R.drawable.cancel);
        loginToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public class SessionCallback implements ISessionCallback {


        @Override
        public void onSessionOpened() {
            redirectSignupActivity();
            Log.d("kakaokkkk", "?");

        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {

            if (exception != null) {
                Log.d("kakao_session_error", exception.toString());
            }
            FacebookSdk.sdkInitialize(LoginActivity.this);
            callbackManager = CallbackManager.Factory.create();
            setContentView(R.layout.activity_login);
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if(Session.getCurrentSession().handleActivityResult(requestCode,resultCode,data)){
//            Log.d("kakao_session_error", "???");
//            return;
//        }
//
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    protected void redirectSignupActivity() {
        Log.d("kakaokkkk", "?");

        final Intent intent = new Intent(this, KakaoSignupActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            callbackManager.onActivityResult(requestCode, resultCode, data);
            return;
        }
    }

    public void facebookLoginOnClick(View v) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this,
                Arrays.asList("public_profile", "email"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(final LoginResult result) {

                GraphRequest request;
                request = GraphRequest.newMeRequest(result.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject user, GraphResponse response) {
                        if (response.getError() != null) {

                        } else {
                            json = user;

                            final Call<CheckId> getCheckId;
                            try {
                                getCheckId = service.getCheckId(json.get("email").toString() + "facebook");
                                getCheckId.enqueue(new Callback<CheckId>() {
                                    @Override
                                    public void onResponse(Call<CheckId> call, Response<CheckId> response) {
                                        if (response.isSuccessful()) {
                                            if (response.body().checkId == 0) {
                                                Profile profile = Profile.getCurrentProfile();

                                                final String link = profile.getProfilePictureUri(200, 200).toString();
                                                setResult(RESULT_OK);

                                                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                                                try {
                                                    i.putExtra("id", json.get("email").toString() + "facebook");
                                                    i.putExtra("profileImage", link);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                startActivity(i);
                                            } else {
                                                try {
                                                    preferencesService.setPrefData("id", json.get("email").toString()+"facebook");
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
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
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            finish();
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("test", "Error: " + error);
                //finish();
            }

            @Override
            public void onCancel() {
                //finish();
            }
        });
    }


}
