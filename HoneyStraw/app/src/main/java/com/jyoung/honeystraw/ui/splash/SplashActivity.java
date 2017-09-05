package com.jyoung.honeystraw.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.jyoung.honeystraw.R;
import com.jyoung.honeystraw.base.util.SharedPreferencesService;
import com.jyoung.honeystraw.ui.tabs.TabActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.jyoung.honeystraw.base.util.FirebaseMessagingService.getLauncherClassName;

public class SplashActivity extends AppCompatActivity {
    SharedPreferencesService preferencesService;
    @InjectView(R.id.splash_fill)ImageView splashFill;
    @InjectView(R.id.splash_main)ImageView splashMain;
    @InjectView(R.id.splash_title)ImageView splashTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.inject(this);
        preferencesService = new SharedPreferencesService();
        preferencesService.load(getApplicationContext());

        Animation scaleAnim = AnimationUtils.loadAnimation(this,R.anim.splash_scale);
        splashFill.startAnimation(scaleAnim);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                finish();
                startActivity(new Intent(getApplicationContext(), TabActivity.class));
            }
        }, 1900);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("sdf", preferencesService.getPrefIntegerData("badgeCount")+"");
        if(preferencesService.getPrefIntegerData("badgeCount") > 0)
            preferencesService.setPrefData("badgeCount", 0);

        Intent badgeIntent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        badgeIntent.putExtra("badge_count", preferencesService.getPrefIntegerData("badgeCount"));
        badgeIntent.putExtra("badge_count_package_name", getPackageName());
        badgeIntent.putExtra("badge_count_class_name", getLauncherClassName(getApplicationContext()));
        sendBroadcast(badgeIntent);
    }
}
