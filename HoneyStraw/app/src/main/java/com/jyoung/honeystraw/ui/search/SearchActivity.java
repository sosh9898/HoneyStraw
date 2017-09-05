package com.jyoung.honeystraw.ui.search;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jyoung.honeystraw.R;
import com.jyoung.honeystraw.application.ApplicationController;
import com.jyoung.honeystraw.base.util.BundleBuilder;
import com.jyoung.honeystraw.base.util.SharedPreferencesService;
import com.jyoung.honeystraw.model.ResultSearch;
import com.jyoung.honeystraw.model.Search;
import com.jyoung.honeystraw.network.NetworkService;

import org.parceler.Parcels;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jyoung.honeystraw.R.array.brandName;

public class SearchActivity extends AppCompatActivity {
    @InjectView(R.id.search_edit)EditText searchEdit;
    @InjectView(R.id.search_auto_rcv)RecyclerView autoRecycler;
    @InjectView(R.id.search_content)FrameLayout frameLayout;
    List<String> searchAutoList;
    AutoSearchRecyclerAdapter autoSearchRecyclerAdapter;


    SharedPreferencesService preferencesService;
    NetworkService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.inject(this);
        service = ApplicationController.getInstance().getNetworkService();
        preferencesService = new SharedPreferencesService();
        preferencesService.load(getApplicationContext());
        setLayout();
        setRecycler();
        if(savedInstanceState == null){
            addFragment(SearchFragment.getInstance(), BundleBuilder.create().with("brandName",brandName).build());
        }
    }

    public void setLayout(){

        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                frameLayout.setVisibility(View.GONE);
                autoRecycler.setVisibility(View.VISIBLE);
                autoSearch(searchEdit.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(searchEdit.getText().toString().equals("")){
                    replaceFragment(SearchFragment.getInstance(),null);
                    frameLayout.setVisibility(View.VISIBLE);
                    autoRecycler.setVisibility(View.GONE);
                }

            }
        });

        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                switch (i){
                    case EditorInfo.IME_ACTION_SEARCH:
                        searchStart(searchEdit.getText().toString());
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    public void setRecycler(){
        autoRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        autoSearchRecyclerAdapter = new AutoSearchRecyclerAdapter(searchAutoList, onClickListener, this);
        autoRecycler.setAdapter(autoSearchRecyclerAdapter);
        autoRecycler.setHasFixedSize(true);
    }

    public int getLength(){
        return searchEdit.getText().length();
    }

    public void searchStart(String searchText){
        hideSoftKeyboard(SearchActivity.this);
        frameLayout.setVisibility(View.VISIBLE);
        autoRecycler.setVisibility(View.GONE);
        Search search = new Search();
        search.userId = preferencesService.getPrefStringData("id");
        search.searchResult = searchText;

        Call<ResultSearch> getSearchResult = service.getSearchResult(search);

        getSearchResult.enqueue(new Callback<ResultSearch>() {
            @Override
            public void onResponse(Call<ResultSearch> call, Response<ResultSearch> response) {
                if(response.isSuccessful()){
                    if(response.body().result.message.equals("ok")) {
                        if (response.body().result.coverList.size() != 0) {
                            Parcelable parcelable = Parcels.wrap(response.body().result.coverList);
                            replaceFragment(SearchResultFragment.getInstance(), BundleBuilder.create().with("coverList", parcelable).build());
                        } else {
                            replaceFragment(SearchResultFragment.getInstance(), null);
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<ResultSearch> call, Throwable t) {

            }
        });
    }

    public void addFragment(Fragment fragment, Bundle bundle){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        fragment.setArguments(bundle);
        transaction.add(R.id.search_content, fragment);
        transaction.commit();
    }

    public void replaceFragment(Fragment fragment, @Nullable Bundle bundle){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        fragment.setArguments(bundle);
        transaction.replace(R.id.search_content, fragment);
        transaction.commit();
    }

    @OnClick(R.id.search_back_image)
    void onClick(View view){
        finish();
    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final int tempPosition = autoRecycler.getChildPosition(view);
            autoRecycler.setVisibility(View.GONE);
            searchStart(searchAutoList.get(tempPosition));

        }
    };

    public void autoSearch(String searchText){
        if(!searchText.equals("")) {
            Call<List<String>> getAutoSearch = service.getSearchAuto(searchText);
            getAutoSearch.enqueue(new Callback<List<String>>() {
                @Override
                public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                    if (response.isSuccessful()) {
                        searchAutoList = response.body();
                        autoSearchRecyclerAdapter.refreshAdapter(searchAutoList);
                    }
                }

                @Override
                public void onFailure(Call<List<String>> call, Throwable t) {

                }
            });
        }
    }
    public void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
}
