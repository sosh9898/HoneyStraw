package com.jyoung.honeystraw.ui.register;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jyoung.honeystraw.R;
import com.jyoung.honeystraw.application.ApplicationController;
import com.jyoung.honeystraw.base.util.SharedPreferencesService;
import com.jyoung.honeystraw.base.util.StringUtil;
import com.jyoung.honeystraw.base.util.ToastMaker;
import com.jyoung.honeystraw.model.Brand;
import com.jyoung.honeystraw.model.RegisterTip;
import com.jyoung.honeystraw.model.RequestResult;
import com.jyoung.honeystraw.model.ReturnTips;
import com.jyoung.honeystraw.network.NetworkService;
import com.jyoung.honeystraw.ui.tabs.TabActivity;
import com.yalantis.ucrop.UCrop;

import org.parceler.Parcels;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.maxHeight;
import static android.R.attr.maxWidth;
import static android.app.Activity.RESULT_OK;
import static com.jyoung.honeystraw.ui.register.RegisterActivity.REQ_CODE_EDIT_BACKGROUND;
import static com.jyoung.honeystraw.ui.register.RegisterActivity.REQ_CODE_EDIT_COVER;
import static com.jyoung.honeystraw.ui.register.RegisterActivity.REQ_CODE_EDIT_TIP;
import static com.jyoung.honeystraw.ui.register.RegisterActivity.REQ_CODE_SELECT_BACKGROUND;
import static com.jyoung.honeystraw.ui.register.RegisterActivity.REQ_CODE_SELECT_COVER;
import static com.jyoung.honeystraw.ui.register.RegisterActivity.REQ_CODE_SELECT_IMAGE;
import static com.jyoung.honeystraw.ui.register.RegisterActivity.REQ_CODE_SELECT_MORE_IMAGE;
import static com.jyoung.honeystraw.ui.tabs.home.HomeTabRecyclerAdapter.TYPE_BASE;
import static com.jyoung.honeystraw.ui.tabs.home.HomeTabRecyclerAdapter.TYPE_FOOTER;
import static com.jyoung.honeystraw.ui.tabs.home.HomeTabRecyclerAdapter.TYPE_FULL;
import static com.jyoung.honeystraw.ui.tabs.home.HomeTabRecyclerAdapter.TYPE_HEADER;

/**
 * Created by jyoung on 2017. 8. 3..
 */

public class RegisterFragment extends Fragment {
    @InjectView(R.id.title_line)
    FrameLayout titleLine;
    @InjectView(R.id.brand_line)
    FrameLayout brandLine;
    @InjectView(R.id.cover_background_line)
    FrameLayout coverLine;
    @InjectView(R.id.tip_base_line)
    FrameLayout baseLine;
    @InjectView(R.id.tip_bottom_line)
    FrameLayout bottomLine;
    @InjectView(R.id.tip_title_edit)
    EditText tipTitleEdit;
    @InjectView(R.id.register_tip_rcv)
    RecyclerView registerRecycler;
    @InjectView(R.id.tip_brand_spinner)
    Spinner tipBrandSpinner;
    @InjectView(R.id.register_background_image)
    ImageView backgroundImage;
    @InjectView(R.id.register_background_text)
    TextView backgroundText;
    @InjectView(R.id.register_background_item)
    CardView backgroundItem;
    @InjectView(R.id.register_cover_item)
    CardView coverItem;
    @InjectView(R.id.register_cover_image)
    ImageView coverImage;
    @InjectView(R.id.register_cover_text)
    TextView coverText;
    @InjectView(R.id.photo1)ImageView photoImage1;
    @InjectView(R.id.photo2)ImageView photoImage2;
    RegisterRecyclerAdapter registerRecyclerAdapter;
    List<RegisterTip> registerTipList;
    List<ReturnTips> returnTipsList;
    List<Brand> brandList;
    String tempBackground;
    String tempCoverUri;
    List<String> imgUrl;
    List<String> tempContent;

    int tipFlag, coverFlag, backgroundFlag = 0;


    MultipartBody.Part[] body;
    File[] files;

    NetworkService service;
    Uri[] data;
    String coverType, scrollType = "";

    CustomCoverDialog customCoverDialog;
    CustomScrollDialog customScrollDialog;

    SharedPreferencesService preferencesService;

    ProgressDialog mProgressDialog;

    public static final int SCROLL_LF = 700;
    public static final int SCROLL_UP = 701;


    public RegisterFragment() {
    }

    public static RegisterFragment getInstance() {
        return new RegisterFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        service = ApplicationController.getInstance().getNetworkService();
        imgUrl = new ArrayList<String>();
        tempContent = new ArrayList<String>();
        preferencesService = new SharedPreferencesService();
        preferencesService.load(getContext());
        setProgressDialog();

        setRecyclerview();
        setSpinner();
        nullCheck();
        setFocus();

    }

    public void setFocus() {
        tipTitleEdit.requestFocus();
        tipTitleEdit.setNextFocusUpId(R.id.tip_brand_spinner);
    }

    public void nullCheck() {
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);


        tipTitleEdit.addTextChangedListener(new TextWatcher() {
            String strCur;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                strCur = s.toString();

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!tipTitleEdit.getText().toString().equals(""))
                    titleLine.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                else
                    titleLine.setBackgroundColor(getResources().getColor(R.color.colorBaseLine));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tipTitleEdit.setImeOptions(EditorInfo.IME_ACTION_DONE);
        tipTitleEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    imm.hideSoftInputFromWindow(tipTitleEdit.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

    }

    public void backgroundFocusing(){
        backgroundItem.requestFocus();
    }

    public void setProgressDialog(){
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("등록 중...");
        mProgressDialog.setIndeterminate(true);
    }

    public List<RegisterTip> setBaseData() {
        registerTipList = new ArrayList<>();
        registerTipList.add(new RegisterTip(TYPE_HEADER, null, null));
        return registerTipList;
    }

    public void setRecyclerview() {
        registerRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        registerRecycler.setHasFixedSize(true);
        registerRecyclerAdapter = new RegisterRecyclerAdapter(setBaseData(), onClickListener, getActivity());
        registerRecycler.setAdapter(registerRecyclerAdapter);
    }

    public void setImage(String[] tempUri) {
        Intent intent = new Intent(getActivity(), RegisterTipEditActivity.class);
        intent.putExtra("selectImages", tempUri);
        intent.putExtra("flag", 0);
        getActivity().startActivityForResult(intent, REQ_CODE_EDIT_TIP);

    }

    public void setBackground(String tempUri) {
        Uri mDestinationUri = Uri.fromFile(new File(getActivity().getCacheDir(), getImageNameToUri(Uri.parse(tempUri))));

        UCrop.Options options = new UCrop.Options();
        options.setToolbarColor(getResources().getColor(R.color.colorPrimary));
        options.setToolbarTitle("꿀팁 제조기");
        options.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        UCrop.of(Uri.parse(tempUri), mDestinationUri)
                .withOptions(options)
                .withAspectRatio(3.7f, 5.6f)
                .withMaxResultSize(maxWidth, maxHeight)
                .start(getActivity(), REQ_CODE_EDIT_BACKGROUND);
    }

    public void setCover(String tempUri) {
        Uri mDestinationUri = Uri.fromFile(new File(getActivity().getCacheDir(), getImageNameToUri(Uri.parse(tempUri))));

        UCrop.Options options = new UCrop.Options();
        options.setToolbarColor(getResources().getColor(R.color.colorPrimary));
        options.setToolbarTitle("꿀팁 제조기");
        options.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        if (coverType.equals("103")) {

            UCrop.of(Uri.parse(tempUri), mDestinationUri)
                    .withOptions(options)
                    .withAspectRatio(2.2f, 2.8f)
                    .withMaxResultSize(maxWidth, maxHeight)
                    .start(getActivity(), REQ_CODE_EDIT_COVER);
        } else if (coverType.equals("101")) {

            UCrop.of(Uri.parse(tempUri), mDestinationUri)
                    .withOptions(options)
                    .withAspectRatio(2.2f, 1.6f)
                    .withMaxResultSize(maxWidth, maxHeight)
                    .start(getActivity(), REQ_CODE_EDIT_COVER);
        }
    }

    public String getImageNameToUri(Uri data) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        String imgPath = cursor.getString(column_index);
        String imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1);

        return imgName;
    }

    public void updateTip(List<ReturnTips> returnTipsList) {
        registerTipList = new ArrayList<>();
        for (int i = 0; i < returnTipsList.size(); i++) {
            registerTipList.add(new RegisterTip(TYPE_BASE, returnTipsList.get(i).getReturnImageUri(), returnTipsList.get(i).getReturnContent()));
        }
        registerTipList.add(new RegisterTip(TYPE_FOOTER, null, null));
        registerRecyclerAdapter.refreshAdapter(registerTipList);
        baseLine.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        bottomLine.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    }

    public void updateBackground(String tempBackground) {
        Glide.with(getContext()).load(tempBackground).into(backgroundImage);
        coverLine.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    }

    public void updateCover(String tempCoverUri) {
        Glide.with(getContext()).load(tempCoverUri).into(coverImage);
    }

    public void setSpinner() {
        brandList = new ArrayList<Brand>();
        brandList.add(new Brand(TYPE_BASE, null, "브랜드를 선택해주세요~",0));
        brandList.add(new Brand(TYPE_BASE, null, "맥도날드",0));
        brandList.add(new Brand(TYPE_BASE, null, "다이소",0));
        brandList.add(new Brand(TYPE_BASE, null, "롭스",0));
        brandList.add(new Brand(TYPE_BASE, null, "서브웨이",0));
        brandList.add(new Brand(TYPE_BASE, null, "스타벅스",0));
        brandList.add(new Brand(TYPE_BASE, null, "올리브영",0));

        tipBrandSpinner.setAdapter(new CustomSpinnerAdapter(getContext(), brandList));
        tipBrandSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(!tipBrandSpinner.getSelectedItem().equals("브랜드를 선택해주세요~")){
                    brandLine.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
                else
                    brandLine.setBackgroundColor(getResources().getColor(R.color.colorBaseLine));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void setAddMore(String tempUri) {
        returnTipsList.add(new ReturnTips(tempUri, null, 0));
        Parcelable parcelable = Parcels.wrap(returnTipsList);
        Intent intent = new Intent(getContext(), RegisterTipEditActivity.class);
        intent.putExtra("EditMore", parcelable);
        intent.putExtra("flag", 2);
        getActivity().startActivityForResult(intent, REQ_CODE_EDIT_TIP);
    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
                final int tempPosition = registerRecycler.getChildPosition(view);
                if (registerTipList.get(tempPosition).getViewType() == TYPE_BASE) {
                    Parcelable parcelable = Parcels.wrap(returnTipsList);
                    Intent intent = new Intent(getContext(), RegisterTipEditActivity.class);
                    intent.putExtra("EditMore", parcelable);
                    intent.putExtra("flag", 1);
                    intent.putExtra("position", tempPosition);
                    getActivity().startActivityForResult(intent, REQ_CODE_EDIT_TIP);
                }
        }
    };

    public void resultCheck(){
        if(StringUtil.isNullOrEmpty(tipTitleEdit.getText().toString())){
            Snackbar.make(getActivity().getWindow().getDecorView().getRootView(), "제목을 입력해주세요!!",Snackbar.LENGTH_SHORT).show();
            tipTitleEdit.requestFocus();
            return;
        }else if(tipBrandSpinner.getSelectedItem().toString().equals("브랜드를 선택해주세요~")){
            Snackbar.make(getActivity().getWindow().getDecorView().getRootView(), "브랜드를 선택해주세요!!",Snackbar.LENGTH_SHORT).show();
            tipBrandSpinner.requestFocus();
            return;
        }
        else if(imgUrl.size()<4){
            Snackbar.make(getActivity().getWindow().getDecorView().getRootView(), "팁은 최소 2개 이상 입력해주세요!!",Snackbar.LENGTH_SHORT).show();
            registerRecycler.requestFocus();
            return;
        }else {
            customScrollDialog = new CustomScrollDialog(getContext(), scrollLeftListener, scrollRightListener);
            customScrollDialog.show();
        }
    }

    @OnClick(R.id.register_background_item)
    void onBackgroundClick(View view) {
            if (imgUrl.size() == 0) {
                Snackbar.make(getActivity().getWindow().getDecorView().getRootView(), "커버를 먼저 선택해주세요!!", Snackbar.LENGTH_SHORT).show();
                coverItem.requestFocus();
                return;
            } else {
                if(backgroundFlag == 0) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, REQ_CODE_SELECT_BACKGROUND);
                }
                else {
                    setBackground(tempBackground);
                }
            }

    }

    public int returnImageUrl(){
        return imgUrl.size();
    }

    @OnClick(R.id.register_cover_item)
    void onSelectCover(View view) {
        if(coverFlag == 0) {
            customCoverDialog = new CustomCoverDialog(getContext(), coverLeftListener, coverRightListener);
            customCoverDialog.show();
        }
        else {
            setCover(tempCoverUri);
        }
    }

    public String selectCoverType() {
        return coverType;
    }

    public String selectScrollType() {
        return scrollType;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            {
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    ClipData clipData = data.getClipData();
                    if (clipData.getItemCount() > 10) {
                        ToastMaker.makeShortToast(getContext(), "사진은 최대 10장입니다!! \n 다시 선택해주세요");
                        return;
                    }
                    String[] tempUri = new String[clipData.getItemCount()];

                    if (clipData != null) {
                        for (int i = 0; i < 10; i++) {
                            if (i < clipData.getItemCount()) {
                                tempUri[i] = clipData.getItemAt(i).getUri().toString();
                            }
                        }
                        setImage(tempUri);
                    } else if (uri != null) {
                        ToastMaker.makeShortToast(getContext(), "??");
                    }
                }
            }
        } else if (requestCode == REQ_CODE_EDIT_TIP) {
            if (resultCode == RESULT_OK) {
                returnTipsList = new ArrayList<>();
                Parcelable parcelable = data.getParcelableExtra("returnTips");
                returnTipsList = Parcels.unwrap(parcelable);
                updateTip(returnTipsList);
                String content = "";
                for (int i = 0; i < returnTipsList.size(); i++) {
                    imgUrl.add(i + 2, returnTipsList.get(i).getReturnImageUri());
                    content = returnTipsList.get(i) != null ? returnTipsList.get(i).getReturnContent() : "";
                    tempContent.add(i, content);
                }
            }
        } else if (requestCode == REQ_CODE_EDIT_BACKGROUND) {
            if (resultCode == RESULT_OK) {
                final Uri resultUri = UCrop.getOutput(data);
                updateBackground(resultUri.toString());
                if(backgroundFlag == 0) {
                    imgUrl.add(1, resultUri.toString());
                    backgroundFlag = 1;
                }else {
                    imgUrl.set(1, resultUri.toString());
                }

                backgroundText.setVisibility(View.VISIBLE);
                photoImage2.setVisibility(View.GONE);
            }
        } else if (requestCode == REQ_CODE_SELECT_BACKGROUND) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                setBackground(uri.toString());
                tempBackground = uri.toString();
            }
        } else if (requestCode == REQ_CODE_SELECT_MORE_IMAGE) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                setAddMore(uri.toString());
            }
        } else if (requestCode == REQ_CODE_SELECT_COVER) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                setCover(uri.toString());
                tempCoverUri = uri.toString();

            }
        } else if (requestCode == REQ_CODE_EDIT_COVER) {
            if (resultCode == RESULT_OK) {
                final Uri resultUri = UCrop.getOutput(data);
                updateCover(resultUri.toString());
                if(coverFlag ==0) {
                    imgUrl.add(0, resultUri.toString());
                    coverFlag =1;
                }else {
                    imgUrl.set(0, resultUri.toString());
                }
                coverText.setVisibility(View.VISIBLE);
                photoImage1.setVisibility(View.GONE);
            }
        }
    }

    private View.OnClickListener scrollLeftListener = new View.OnClickListener() {
        public void onClick(View v) {
            scrollType = String.valueOf(SCROLL_LF);
            registerResultData();
        }
    };

    private View.OnClickListener scrollRightListener = new View.OnClickListener() {
        public void onClick(View v) {
            scrollType = String.valueOf(SCROLL_UP);
            registerResultData();
        }
    };



    private View.OnClickListener coverLeftListener = new View.OnClickListener() {
        public void onClick(View v) {
            coverType = String.valueOf(TYPE_FULL);
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQ_CODE_SELECT_COVER);
            customCoverDialog.dismiss();

        }
    };

    private View.OnClickListener coverRightListener = new View.OnClickListener() {
        public void onClick(View v) {
            coverType = String.valueOf(TYPE_BASE);
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQ_CODE_SELECT_COVER);
            customCoverDialog.dismiss();

        }
    };

    public void registerResultData() {

        customScrollDialog.dismiss();
        mProgressDialog.show();

        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        Date date = Calendar.getInstance().getTime();

        RequestBody title = RequestBody.create(MediaType.parse("multipart/form-data"), tipTitleEdit.getText().toString());
        RequestBody brand = RequestBody.create(MediaType.parse("multipart/form-data"), tipBrandSpinner.getSelectedItem().toString());
        RequestBody coverType = RequestBody.create(MediaType.parse("multipart/form-data"), selectCoverType());
        RequestBody userId = RequestBody.create(MediaType.parse("multipart/form-data"), preferencesService.getPrefStringData("id"));
        RequestBody scrollType = RequestBody.create(MediaType.parse("multipart/form-data"), selectScrollType());
        RequestBody[] content = new RequestBody[returnTipsList.size()];
        RequestBody postdate = RequestBody.create(MediaType.parse("multipart/form-data"), dateformat.format(date));

        for (int i = 0; i < returnTipsList.size(); i++) {
            content[i] = RequestBody.create(MediaType.parse("multipart/form-data"), returnTipsList.get(i).getReturnContent());
        }
        body = new MultipartBody.Part[imgUrl.size()];
        files = new File[imgUrl.size()];

        for (int i = 0; i < imgUrl.size(); i++) {
            if (imgUrl.get(i) == "") {
                body[i] = null;
            } else {
                Log.d("imgurl", imgUrl.get(i).toString());
                BitmapFactory.Options options = new BitmapFactory.Options();

                InputStream in = null;
                try {
                    in = getActivity().getContentResolver().openInputStream(Uri.parse(imgUrl.get(i)));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);

                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                RequestBody photoBody = RequestBody.create(MediaType.parse("image/jpg"), baos.toByteArray());

                files[i] = new File(imgUrl.get(i));

                body[i] = MultipartBody.Part.createFormData("image", files[i].getName(), photoBody);
                bitmap.recycle();
            }
        }



        Call<RequestResult> registerTip = service.registerTip(body, title, brand, scrollType, coverType, userId, content, postdate);
        registerTip.enqueue(new Callback<RequestResult>() {
            @Override
            public void onResponse(Call<RequestResult> call, Response<RequestResult> response) {
                if (response.isSuccessful()) {
                    if (response.body().message.equals("ok"))
                    startActivity(new Intent(getContext(), TabActivity.class));
                    mProgressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<RequestResult> call, Throwable t) {
                Log.d("registerTip error : ", t.toString());
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

}

