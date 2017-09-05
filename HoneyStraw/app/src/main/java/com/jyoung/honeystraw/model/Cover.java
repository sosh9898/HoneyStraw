package com.jyoung.honeystraw.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

/**
 * Created by jyoung on 2017. 8. 1..
 */

@Parcel
public class Cover {
    @SerializedName("num")
    public int coverNum;
    @SerializedName("cover_image")
    public String coverImage;
    @SerializedName("cover_title")
    public String coverContent;
    @SerializedName("user_id")
    public String userId;
    @SerializedName("cover_type")
    public int coverType;
    @SerializedName("scroll_type")
    public int scrollType;
    @SerializedName("background_image")
    public String backgroundImage;
    @SerializedName("straw_num")
    public int strawNum;
    @SerializedName("comment_num")
    public int commentNum;
    public String brand;
    @SerializedName("view_count")
    public int viewCount;
    public String postdate;

    public String getPostdate() {
        return postdate;
    }

    public void setPostdate(String postdate) {
        this.postdate = postdate;
    }

    public Cover() {
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public int getStrawState() {
        return strawState;
    }

    public void setStrawState(int strawState) {
        this.strawState = strawState;
    }

    @SerializedName("straw_state")
    public int strawState;

    @ParcelConstructor
    public Cover(int coverType) {
        this.coverType = coverType;
    }

    public int getCoverNum() {
        return coverNum;
    }

    public void setCoverNum(int coverNum) {
        this.coverNum = coverNum;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getCoverContent() {
        return coverContent;
    }

    public void setCoverContent(String coverContent) {
        this.coverContent = coverContent;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getCoverType() {
        return coverType;
    }

    public void setCoverType(int coverType) {
        this.coverType = coverType;
    }

    public int getScrollType() {
        return scrollType;
    }

    public void setScrollType(int scrollType) {
        this.scrollType = scrollType;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public int getStrawNum() {
        return strawNum;
    }

    public void setStrawNum(int strawNum) {
        this.strawNum = strawNum;
    }
}

