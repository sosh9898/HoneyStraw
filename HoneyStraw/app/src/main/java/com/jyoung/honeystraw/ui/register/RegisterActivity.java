package com.jyoung.honeystraw.ui.register;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.jyoung.honeystraw.R;
import com.jyoung.honeystraw.base.util.BundleBuilder;
import com.yalantis.ucrop.UCrop;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.jyoung.honeystraw.R.array.brandName;

public class RegisterActivity extends AppCompatActivity {
    @InjectView(R.id.register_content)
    FrameLayout registerContent;
    @InjectView(R.id.register_toolbar)
    Toolbar registerToolbar;
    static final int REQ_CODE_SELECT_IMAGE = 300;
    static final int REQ_CODE_EDIT_TIP = 301;
    static final int REQ_CODE_EDIT_BACKGROUND = 302;
    static final int REQ_CODE_SELECT_BACKGROUND = 303;
    static final int REQ_CODE_SELECT_MORE_IMAGE = 304;
    static final int REQ_CODE_SELECT_COVER = 305;
    static final int REQ_CODE_EDIT_COVER = 306;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.inject(this);
        setToolbar();
        getPermission();
        if (savedInstanceState == null) {
            AddFragment(RegisterFragment.getInstance(), BundleBuilder.create().with("brandName", brandName).build());
        }

    }
    public void getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int permissionRead = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

            if (permissionRead == PackageManager.PERMISSION_DENIED) {
                PermissionListener permissionListener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        finish();

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
        registerToolbar.setTitle("꿀팁 등록");
        registerToolbar.setTitleTextColor(getResources().getColor(R.color.splashBackground));
        setSupportActionBar(registerToolbar);
        registerToolbar.setNavigationIcon(R.drawable.left_arrow);
        registerToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void AddFragment(Fragment fragment, Bundle bundle) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        fragment.setArguments(bundle);
        transaction.add(R.id.register_content, fragment);
        transaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if (requestCode == REQ_CODE_SELECT_IMAGE) {
                {
                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.register_content);
                    if (fragment != null) {
                        ((RegisterFragment) fragment).onActivityResult(requestCode, resultCode, data);
                    }
                }
            } else if (requestCode == REQ_CODE_EDIT_TIP) {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.register_content);
                if (fragment != null) {
                    ((RegisterFragment) fragment).onActivityResult(requestCode, resultCode, data);
                }

            } else if (requestCode == REQ_CODE_EDIT_BACKGROUND) {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.register_content);
                if (fragment != null) {
                    ((RegisterFragment) fragment).onActivityResult(requestCode, resultCode, data);
                }
            } else if (requestCode == REQ_CODE_SELECT_BACKGROUND) {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.register_content);
                if (fragment != null) {
                    ((RegisterFragment) fragment).onActivityResult(requestCode, resultCode, data);
                }
            } else if (requestCode == REQ_CODE_SELECT_MORE_IMAGE) {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.register_content);
                if (fragment != null) {
                    ((RegisterFragment) fragment).onActivityResult(requestCode, resultCode, data);
                }
            } else if (requestCode == REQ_CODE_SELECT_COVER) {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.register_content);
                if (fragment != null) {
                    ((RegisterFragment) fragment).onActivityResult(requestCode, resultCode, data);
                }
            }
            else if (requestCode == REQ_CODE_EDIT_COVER) {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.register_content);
                if (fragment != null) {
                    ((RegisterFragment) fragment).onActivityResult(requestCode, resultCode, data);
                }
            }
            else if (requestCode == UCrop.REQUEST_CROP){
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.register_content);
                if (fragment != null) {
                    ((RegisterFragment) fragment).onActivityResult(requestCode, resultCode, data);
                }
            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.upload_setting) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.register_content);
            ((RegisterFragment)fragment).resultCheck();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.register, menu);
        return true;
    }
}





