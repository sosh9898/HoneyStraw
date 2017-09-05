package com.jyoung.honeystraw.ui.tip;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jyoung.honeystraw.R;
import com.jyoung.honeystraw.application.ApplicationController;
import com.jyoung.honeystraw.base.util.SharedPreferencesService;
import com.jyoung.honeystraw.base.util.ToastMaker;
import com.jyoung.honeystraw.model.DetailTips;
import com.jyoung.honeystraw.model.ResultStraw;
import com.jyoung.honeystraw.network.NetworkService;
import com.jyoung.honeystraw.ui.login.LoginActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jyoung.honeystraw.ui.tabs.home.HomeTabRecyclerAdapter.STRAW_CHECKED;
import static com.jyoung.honeystraw.ui.tabs.home.HomeTabRecyclerAdapter.STRAW_UNCHECKED;

public class DetailTipActivity extends AppCompatActivity {
    @InjectView(R.id.bottom_bar)
    RelativeLayout bottomBar;
    @InjectView(R.id.tip_viewpager)
    ViewPager tipviewPager;
    @InjectView(R.id.bottom_bar_page_text)
    TextView pageText;
    @InjectView(R.id.bottom_bar_comment_text)
    TextView commentText;
    @InjectView(R.id.bottom_bar_comment_image)
    ImageView commentImage;
    @InjectView(R.id.bottom_bar_comment)LinearLayout commentItem;
    @InjectView(R.id.bottom_bar_straw_text)
    TextView strawText;
    @InjectView(R.id.detail_tip_background_image)
    ImageView backgroundImage;
    @InjectView(R.id.bottom_bar_straw_image)ImageView strawImage;
    @InjectView(R.id.arrow_right)ImageView arrowImage;
    DetailTipPagerAdatper tipviewPagerAdapter;
    NetworkService service;
    int flag = 0;
    int tempNum, tempState, tempStrawNum, tempCommentNum, tempViewCount;
    String tempUserId, tempBackground, tempTitle, tempBrandName, tempPostdate;
    DetailTips.ResultData resultData;

    public static final int HIDE_TIP_TEXT = 130;
    public static final int SHOW_TIP_TEXT = 131;

    public static final int REQ_COMMENT_BACK = 150;
    SharedPreferencesService preferencesService;

    Animation animationFadein;
    Animation animationFadeout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tip);
        ButterKnife.inject(this);

        preferencesService = new SharedPreferencesService();
        preferencesService.load(getApplicationContext());
        Intent intent = getIntent();
        tempNum = intent.getExtras().getInt("num");
        tempUserId = intent.getExtras().getString("userId");
        tempTitle = intent.getExtras().getString("title");
        tempBackground = intent.getExtras().getString("background");
        tempState = intent.getExtras().getInt("state");
        tempStrawNum = intent.getExtras().getInt("strawNum");
        tempCommentNum = intent.getExtras().getInt("commentNum");
        tempBrandName = intent.getExtras().getString("brandName");
        tempPostdate = intent.getExtras().getString("postdate");
        tempViewCount = intent.getExtras().getInt("viewCount");

        commentItem.setOnTouchListener(onTouchListener);
        commentText.setText(tempCommentNum + "");
        Log.d(">>>>", tempStrawNum+"");

        strawText.setText(tempStrawNum + "");

        if(tempState == STRAW_CHECKED)
            Glide.with(getApplicationContext()).load(R.drawable.straw_checked).into(strawImage);
        else if(tempState == STRAW_UNCHECKED)
            Glide.with(getApplicationContext()).load(R.drawable.straw_unchecked).into(strawImage);

         animationFadein = new AlphaAnimation(0.0f, 1.0f);
         animationFadeout = new AlphaAnimation(1.0f, 0.0f);
        fadeAnimation(arrowImage);
        networking();
    }

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == HIDE_TIP_TEXT) {
                handler.post(hide);
            } else if (msg.what == SHOW_TIP_TEXT){
                handler.post(show);
            }
            else
                handler.post(update);
        }
    };

    final Runnable update = new Runnable() {
        public void run() {
            pageText.setText((tipviewPager.getCurrentItem() + 1) + "/" + tipviewPagerAdapter.getCount());
            if(tipviewPager.getCurrentItem() != 0){
                animationFadein.cancel();
                animationFadeout.cancel();
                arrowImage.setVisibility(View.GONE);
            }
        }
    };

    public void setLayout() {
        Glide.with(getApplicationContext()).load(tempBackground).into(backgroundImage);
        tipviewPagerAdapter = new DetailTipPagerAdatper(getSupportFragmentManager(), resultData.tipContents.size(), resultData);
        tipviewPager.setAdapter(tipviewPagerAdapter);
        tipviewPager.setOffscreenPageLimit(resultData.tipContents.size()+1);
        tipviewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                handler.sendEmptyMessage(tipviewPager.getCurrentItem());
            }

            @Override
            public void onPageSelected(int position) {
                handler.sendEmptyMessage(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tipviewPager.setOnTouchListener(new View.OnTouchListener() {
            private float pointX;
            private float pointY;
            private int tolerance = 50;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        return false;
                    case MotionEvent.ACTION_DOWN:
                        pointX = event.getX();
                        pointY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        boolean sameX = pointX + tolerance > event.getX() && pointX - tolerance < event.getX();
                        boolean sameY = pointY + tolerance > event.getY() && pointY - tolerance < event.getY();
                        if (sameX && sameY) {
                            if (flag == 0) {
                                flag = 1;
                                bottomBar.setVisibility(View.INVISIBLE);
                                handler.sendEmptyMessage(HIDE_TIP_TEXT);
                            } else if (flag == 1) {
                                flag = 0;
                                bottomBar.setVisibility(View.VISIBLE);
                                handler.sendEmptyMessage(SHOW_TIP_TEXT);

                            }
                        }
                }
                return false;
            }
        });
        tipviewPager.setCurrentItem(0);
    }

    final Runnable hide = new Runnable() {
        public void run() {
            Fragment fragment = tipviewPagerAdapter.getRegisteredFragment(tipviewPager.getCurrentItem());
            if (tipviewPager.getCurrentItem() != 0)
                ((DetailTipFragment) fragment).tipText.setVisibility(View.INVISIBLE);
        }
    };
    final Runnable show = new Runnable() {
        public void run() {
            if (tipviewPager.getCurrentItem() != 0) {
                Fragment fragment = tipviewPagerAdapter.getRegisteredFragment(tipviewPager.getCurrentItem());
                ((DetailTipFragment) fragment).tipText.setVisibility(View.VISIBLE);
            }
        }
    };

    @OnClick(R.id.bottom_bar_comment)
    void onClickComment(View view) {
        if(!preferencesService.getPrefStringData("id").equals("")) {
            Intent intent = new Intent(getApplicationContext(), DetailTipCommentActivity.class);
            intent.putExtra("coverNum", tempNum);
            startActivityForResult(intent, REQ_COMMENT_BACK);
        }
        else{
            ToastMaker.makeShortToast(getApplicationContext(),"로그인이 필요한 기능입니다.");
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @OnClick(R.id.bottom_bar_straw)
    void onClickStraw(View view) {
        if(!preferencesService.getPrefStringData("id").equals("")) {
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = Calendar.getInstance().getTime();
            String postdate = dateformat.format(date);
            Call<ResultStraw> strawSelector = service.strawSelector(tempNum, preferencesService.getPrefStringData("id"), tempState, postdate);

            strawSelector.enqueue(new Callback<ResultStraw>() {
                @Override
                public void onResponse(Call<ResultStraw> call, Response<ResultStraw> response) {
                    if (response.isSuccessful()) {
                        if (response.body().message.equals("ok")) {
                            strawText.setText(response.body().strawNum + "");
                            tempState = tempState == STRAW_CHECKED ? STRAW_UNCHECKED : STRAW_CHECKED;
                            if (tempState == STRAW_CHECKED)
                                Glide.with(getApplicationContext()).load(R.drawable.straw_checked).into(strawImage);
                            else if (tempState == STRAW_UNCHECKED)
                                Glide.with(getApplicationContext()).load(R.drawable.straw_unchecked).into(strawImage);

                        }
                    }
                }

                @Override
                public void onFailure(Call<ResultStraw> call, Throwable t) {
                    Log.d("err", t.toString());
                }
            });
        }
        else{
            ToastMaker.makeShortToast(getApplicationContext(),"로그인이 필요한 기능입니다.");
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void networking() {
        service = ApplicationController.getInstance().getNetworkService();
        Call<DetailTips> reqDetailTips = service.getDetailTip(tempNum, tempUserId);

        reqDetailTips.enqueue(new Callback<DetailTips>() {
            @Override
            public void onResponse(Call<DetailTips> call, Response<DetailTips> response) {
                if (response.isSuccessful()) {
                    resultData = response.body().result;
                    setLayout();
                }
            }

            @Override
            public void onFailure(Call<DetailTips> call, Throwable t) {
                Log.d("error", t.toString());
            }
        });
    }

    public String getCoverTitle() {
        return tempTitle;
    }

    public String getBrandName() {
        return tempBrandName;
    }

    public int getViewCount() { return tempViewCount; }

    public String getTempPostdate() {
        return tempPostdate;
    }

    @Override
    public void onBackPressed() {
       finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_COMMENT_BACK) {
                commentText.setText(data.getExtras().getInt("commentCount") + "");
            }
        }
    }

    public void fadeAnimation(final View tv) {
            tv.startAnimation(animationFadein);
            tv.startAnimation(animationFadeout);
            animationFadein.setDuration(800);
            animationFadeout.setDuration(500);
            animationFadeout.setStartOffset(animationFadein.getDuration());
            animationFadeout.setRepeatMode(Animation.REVERSE);
            animationFadeout.setRepeatCount(Animation.INFINITE);
    }

    public View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:
                    commentImage.setImageResource(R.drawable.comment_notify);
                    break;
                case MotionEvent.ACTION_UP:
                    commentImage.setImageResource(R.drawable.comment);
                    break;
            }
            return false;
        }
    };
}
