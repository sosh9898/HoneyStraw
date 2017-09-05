package com.jyoung.honeystraw.ui.tabs;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.jyoung.honeystraw.R;
import com.jyoung.honeystraw.application.ApplicationController;
import com.jyoung.honeystraw.base.util.SharedPreferencesService;
import com.jyoung.honeystraw.base.util.ToastMaker;
import com.jyoung.honeystraw.model.RequestResult;
import com.jyoung.honeystraw.model.TokenUpdate;
import com.jyoung.honeystraw.network.NetworkService;
import com.jyoung.honeystraw.ui.login.LoginActivity;
import com.jyoung.honeystraw.ui.mypage.MyPageActivity;
import com.jyoung.honeystraw.ui.register.RegisterActivity;
import com.jyoung.honeystraw.ui.search.SearchActivity;
import com.jyoung.honeystraw.ui.tabs.brand.CustomSortTempDialog;
import com.jyoung.honeystraw.ui.tabs.home.HomeTabFragment;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jyoung.honeystraw.ui.tabs.brand.BrandDetailRecyclerAdapter.SORT_TYPE_COMMENT;
import static com.jyoung.honeystraw.ui.tabs.brand.BrandDetailRecyclerAdapter.SORT_TYPE_RECENTLY;
import static com.jyoung.honeystraw.ui.tabs.brand.BrandDetailRecyclerAdapter.SORT_TYPE_STRAW;
import static com.jyoung.honeystraw.ui.tabs.brand.BrandDetailRecyclerAdapter.SORT_TYPE_VIEWCOUNT;

public class TabActivity extends AppCompatActivity {
    @InjectView(R.id.home_fab)
    public FloatingActionButton fab;
    @InjectView(R.id.tablayout)
    TabLayout tabLayout;
    @InjectView(R.id.viewpager)
    public ViewPager viewpager;
    @InjectView(R.id.toolbar)
    Toolbar mainToolbar;

    int sortType;

    MainPagerAdapter mainPagerAdapter;
    NetworkService service;

    CustomSortTempDialog customSortTempDialog;
    SharedPreferencesService preferencesService;

    private final int PAGE_CHANGE =1;
    private final long FINSH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;
    private final int HIDE_SORT = 77;
    private final int SHOW_SORT = 76;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        service = ApplicationController.getInstance().getNetworkService();

        customSortTempDialog = new CustomSortTempDialog(TabActivity.this,recentlyListener,viewcountListener,strawListener,commentListener);
        preferencesService = new SharedPreferencesService();
        preferencesService.load(getApplicationContext());
        setToolbar();
        setLayout();
        setFloating();

        getPermission();

        if(preferencesService.getPrefStringData("id") != null)
            if(preferencesService.getPrefStringData("token") != null)
            getToken();
    }

    public void getPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                PermissionListener permissionListener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                    }
                };
                new TedPermission(this)
                        .setPermissionListener(permissionListener)
                        .setRationaleMessage("꿀빨대를 100% 이용하기 위한 권한을 주세요!!")
                        .setDeniedMessage("왜 거부하셨어요...\n하지만 [설정] > [권한] 에서 권한을 허용할 수 있어요.")
                        .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .check();
            } else {
                return;
            }
        }
    }

    public void setToolbar() {
        mainToolbar.setTitle("");
        mainToolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        mainToolbar.setNavigationIcon(R.drawable.main_title);
        mainToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLayout();
            }
        });
        setSupportActionBar(mainToolbar);
    }

    public void setLayout() {
        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(mainPagerAdapter);
        tabLayout.setupWithViewPager(viewpager);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                handler.sendEmptyMessage(PAGE_CHANGE);
                supportInvalidateOptionsMenu();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        reduceMarginsInTabs(tabLayout, 60);
        viewpager.setCurrentItem(0);


        setTab();
    }

    public void setTab() {
        String[] tabName = getResources().getStringArray(R.array.tabName);
        for (int i = 0; i < 4; i++) {
            tabLayout.getTabAt(i).setText(tabName[i]);
        }
    }

    public void setFloating() {
        fab.setImageResource(R.drawable.plus_symbol);
        if (viewpager.getCurrentItem() != 0) fab.hide();
    }


    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINSH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
        } else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "뒤로 가기 키을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.home_fab)
    void fabClick(View view) {
        if(!preferencesService.getPrefStringData("id").equals(""))
        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
        else {
                ToastMaker.makeShortToast(getApplicationContext(),"로그인이 필요한 기능입니다.");
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            if(!preferencesService.getPrefStringData("id").equals("")) {
                Intent intent = new Intent(getApplicationContext(), MyPageActivity.class);
                intent.putExtra("userid", preferencesService.getPrefStringData("id"));
                startActivity(intent);
            }
            else{
                    ToastMaker.makeShortToast(getApplicationContext(),"로그인이 필요한 기능입니다.");
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
            }

        } else if (id == R.id.action_login) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }else if(id == R.id.search_main_settings){
            startActivity(new Intent(getApplicationContext(), SearchActivity.class));
        }else if(id == R.id.sort_main_settings){
            customSortTempDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem sort = menu.findItem(R.id.sort_main_settings);
        if(viewpager.getCurrentItem()==0)
            sort.setVisible(true);
        else
            sort.setVisible(false);

        if (!preferencesService.getPrefStringData("id").equals("")) {
            menu.getItem(0).setVisible(false);

        } else {
            menu.getItem(0).setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public void getToken(){
        TokenUpdate tokenUpdate = new TokenUpdate();
        tokenUpdate.token = preferencesService.getPrefStringData("token");
        tokenUpdate.userId = preferencesService.getPrefStringData("id");

        Call<RequestResult> getToken = service.updateToken(tokenUpdate);

        getToken.enqueue(new Callback<RequestResult>() {
            @Override
            public void onResponse(Call<RequestResult> call, Response<RequestResult> response) {
                if(response.isSuccessful()){
                    if(response.body().message.equals("ok")){
                    }
                }
            }

            @Override
            public void onFailure(Call<RequestResult> call, Throwable t) {

            }
        });
    }


    public void reduceMarginsInTabs(TabLayout tabLayout, int marginOffset) {

        View tabStrip = tabLayout.getChildAt(0);
        if (tabStrip instanceof ViewGroup) {
            ViewGroup tabStripGroup = (ViewGroup) tabStrip;
            for (int i = 0; i < ((ViewGroup) tabStrip).getChildCount(); i++) {
                View tabView = tabStripGroup.getChildAt(i);
                if (tabView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                    ((ViewGroup.MarginLayoutParams) tabView.getLayoutParams()).leftMargin = marginOffset;
                    ((ViewGroup.MarginLayoutParams) tabView.getLayoutParams()).rightMargin = marginOffset;
                }
            }

            tabLayout.requestLayout();
        }
    }

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
          if(msg.what == 1)
                handler.post(fabControl);
        }
    };

    final Runnable fabControl = new Runnable() {
        public void run() {
            if(viewpager.getCurrentItem()==3)
                hideFAB();
            else
                showFAB();
        }
    };

    public View.OnClickListener recentlyListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            sortType = SORT_TYPE_RECENTLY;
            Fragment fragment = mainPagerAdapter.getRegisteredFragment(0);
            ((HomeTabFragment)fragment).networking(sortType,0, 0);
            ((HomeTabFragment)fragment).setStartIndex();
            customSortTempDialog.dismiss();
        }
    };
    public View.OnClickListener viewcountListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            sortType = SORT_TYPE_VIEWCOUNT;
            Fragment fragment = mainPagerAdapter.getRegisteredFragment(0);
            ((HomeTabFragment)fragment).networking(sortType,0, 0);
            ((HomeTabFragment)fragment).setStartIndex();
            customSortTempDialog.dismiss();
        }
    };
    public View.OnClickListener strawListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            sortType = SORT_TYPE_STRAW;
            Fragment fragment = mainPagerAdapter.getRegisteredFragment(0);
            ((HomeTabFragment)fragment).networking(sortType,0, 0);
            ((HomeTabFragment)fragment).setStartIndex();
            customSortTempDialog.dismiss();
        }
    };
    public View.OnClickListener commentListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            sortType = SORT_TYPE_COMMENT;
            Fragment fragment = mainPagerAdapter.getRegisteredFragment(0);
            ((HomeTabFragment)fragment).networking(sortType,0, 0);
            ((HomeTabFragment)fragment).setStartIndex();
            customSortTempDialog.dismiss();
        }
    };


    public void hideFAB(){
        fab.hide();
    }
    public void showFAB(){
        fab.show();
    }

}
