package com.jyoung.honeystraw.network;

import com.jyoung.honeystraw.model.CheckId;
import com.jyoung.honeystraw.model.Comment;
import com.jyoung.honeystraw.model.CommentResult;
import com.jyoung.honeystraw.model.Cover;
import com.jyoung.honeystraw.model.DetailTips;
import com.jyoung.honeystraw.model.Notify;
import com.jyoung.honeystraw.model.RequestResult;
import com.jyoung.honeystraw.model.ResultLike;
import com.jyoung.honeystraw.model.ResultSearch;
import com.jyoung.honeystraw.model.ResultStraw;
import com.jyoung.honeystraw.model.Search;
import com.jyoung.honeystraw.model.SearchRecently;
import com.jyoung.honeystraw.model.TokenUpdate;
import com.jyoung.honeystraw.model.User;
import com.jyoung.honeystraw.model.UserPage;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by Jyoung on 2017-07-18.
 */

public interface NetworkService {

    @GET("/mainlist/{sortType}/{startIndex}")
    Call<List<Cover>> getMainList(@Path("sortType") int sortType, @Path("startIndex") int startIndex);

    @GET("/mainlist/after/{userid}/{sortType}/{startIndex}")
    Call<List<Cover>> getMainListAfterLogin(@Path("userid") String userid, @Path("sortType") int sortType, @Path("startIndex") int startIndex);

    @GET("/mainlist/notify/{userid}/{num}")
    Call<Cover> getTipInfoNotify(@Path("userid") String userid, @Path("num") int num);

    @GET("/mainlist/brand/{brand}/{sortType}")
    Call<List<Cover>> getDetailList(@Path("brand") String brand, @Path("sortType") int sortType);

    @Multipart
    @POST("/tipregister")
    Call<RequestResult> registerTip(@Part MultipartBody.Part[] files,
                                    @Part("title") RequestBody title,
                                    @Part("brand") RequestBody brand,
                                    @Part("scrollType") RequestBody scrollType,
                                    @Part("coverType") RequestBody coverType,
                                    @Part("userId") RequestBody userId,
                                    @Part("content") RequestBody[] content,
                                    @Part("postdate") RequestBody postdate);

    @Multipart
    @POST("/signup")
    Call<RequestResult> registerUser(@Part MultipartBody.Part file,
                                     @Part("id") RequestBody id,
                                     @Part("nickname") RequestBody nickname,
                                     @Part("stateMessage") RequestBody stateMessage);

    @POST("/signup/noEdit")
    Call<RequestResult> registerNoEditUser(@Body User user);

    @GET("/mainlist/detailtip/{num}/{userid}")
    Call<DetailTips> getDetailTip(@Path("num") int num, @Path("userid") String userid);

    @GET("/userpage/{userid}")
    Call<UserPage> getUserInfo(@Path("userid") String userid);

    @GET("/mainlist/straw/{num}/{userid}/{state}/{time}")
    Call<ResultStraw> strawSelector(@Path("num") int num, @Path("userid") String userid, @Path("state") int state, @Path("time") String time);

    @GET("/straw/list/{userid}")
    Call<List<Cover>> getStrawList(@Path("userid") String userid);

    @GET("/mainlist/comment/{num}/{id}")
    Call<CommentResult> getCommentList(@Path("num") int num, @Path("id") String id);

    @POST("/mainlist/comment/{num}")
    Call<RequestResult> registerComment(@Path("num") int num, @Body Comment comment);

    @POST("/search")
    Call<ResultSearch> getSearchResult(@Body Search search);

    @GET("/search/recently/{id}")
    Call<List<SearchRecently>> getRecentlySearchList(@Path("id") String id);

    @GET("/search/delete/{id}")
    Call<RequestResult> getRecordReset(@Path("id") String id);

    @POST("/signup/token")
    Call<RequestResult> updateToken(@Body TokenUpdate tokenUpdate);

    @POST("/userpage/edit")
    Call<RequestResult> editProfile(@Body User user);

    @Multipart
    @POST("/userpage/editimage")
    Call<RequestResult> editProfileWithImage(@Part MultipartBody.Part file,
                                    @Part("id") RequestBody id,
                                    @Part("nickname") RequestBody nickname,
                                    @Part("state_message") RequestBody state_message);
    @GET("/userpage/modify/{id}")
    Call<User> getProfile(@Path("id") String id);

    @GET("/signup/check/{id}")
    Call<CheckId> getCheckId(@Path("id") String id);

    @GET("/mainlist/comment/like/{covernum}/{num}/{id}/{state}/{time}")
    Call<ResultLike> likeselector(@Path("covernum") int covernum, @Path("num") int num, @Path("id") String id, @Path("state") int state, @Path("time") String time);

    @GET("/notify/pagination/{id}/{startIndex}")
    Call<List<Notify>> getNotification(@Path("id") String id, @Path("startIndex") int startIndex);

    @GET("/notify/check/{num}")
    Call<RequestResult> updateCheckNotify(@Path("num") int num);

    @GET("/notify/hide/{num}")
    Call<RequestResult> deleteNotify(@Path("num") int num);

    @GET("/userpage/delete/mytip/{num}")
    Call<RequestResult> deleteMyTip(@Path("num") int num);

    @GET("/comment/delete/{num}/{coverNum}")
    Call<RequestResult> deleteMyComment(@Path("num") int num, @Path("coverNum") int coverNum);

    @GET("/search/auto/{searchText}")
    Call<List<String>> getSearchAuto(@Path("searchText") String searchText);
}
