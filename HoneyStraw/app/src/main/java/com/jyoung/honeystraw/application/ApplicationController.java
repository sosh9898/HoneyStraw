package com.jyoung.honeystraw.application;

import android.app.Activity;
import android.app.Application;

import com.jyoung.honeystraw.base.util.KakaoSDKAdapter;
import com.jyoung.honeystraw.network.NetworkService;
import com.kakao.auth.KakaoSDK;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApplicationController extends Application {

    private static ApplicationController instance;

    private String baseUrl = "";

    private NetworkService networkService;

    public static ApplicationController getInstance() {
        return instance;
    }

    public NetworkService getNetworkService() {
        return networkService;
    }


    public static volatile Activity currentActivity = null;

    @Override
    public void onCreate() {
        super.onCreate();

        ApplicationController.instance = this;
        buildService();
        KakaoSDK.init(new KakaoSDKAdapter());
    }

    public void buildService() {
        Retrofit.Builder builder = new Retrofit.Builder();
        Retrofit retrofit = builder
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        networkService = retrofit.create(NetworkService.class);
    }

    public static Activity getCurrentActivity(){
        return currentActivity;
    }

    public static void setCurrentActivity(Activity currentActivity){
        ApplicationController.currentActivity = currentActivity;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;
    }
}