package com.jyoung.honeystraw.ui.mypage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.jyoung.honeystraw.R;
import com.jyoung.honeystraw.base.util.BundleBuilder;
import com.jyoung.honeystraw.base.util.SharedPreferencesService;
import com.jyoung.honeystraw.ui.tabs.TabActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MyPageActivity extends AppCompatActivity {
    @InjectView(R.id.mypage_content)FrameLayout myPageContent;
    @InjectView(R.id.mypage_toolbar)Toolbar myPageToolbar;

    String tempid;
    String tempNick;
    String tempTitle;
    SharedPreferencesService preferencesService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);
        ButterKnife.inject(this);
        preferencesService = new SharedPreferencesService();
        preferencesService.load(getApplicationContext());

        Intent intent = getIntent();
        tempid = intent.getExtras().getString("userid");
        tempNick = intent.getExtras().getString("nickname");
        setToolbar();

        Log.d("ididid", tempid);

        if(savedInstanceState == null){
            AddFragment(MyPageFragment.getInstance(), BundleBuilder.create().with("id", tempid).build());
        }
    }

    public void setToolbar(){
        if(preferencesService.getPrefStringData("id").equals(tempid))
            tempTitle = "마이페이지";
        else
            tempTitle = tempNick;

        myPageToolbar.setTitle(tempTitle);
        myPageToolbar.setTitleTextColor(getResources().getColor(R.color.splashBackground));
        setSupportActionBar(myPageToolbar);

        myPageToolbar.setNavigationIcon(R.drawable.left_arrow);
        myPageToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void AddFragment(Fragment fragment, Bundle bundle){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        fragment.setArguments(bundle);
        transaction.add(R.id.mypage_content, fragment);
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            preferencesService.removeData("id");
            Intent intent = new Intent(getApplicationContext(), TabActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.action_modify){
            Intent intent = new Intent(getApplicationContext(), MyPageEditActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(preferencesService.getPrefStringData("id").equals(tempid)) {
            getMenuInflater().inflate(R.menu.mypage, menu);
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
