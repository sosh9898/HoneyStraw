package com.jyoung.honeystraw.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jyoung on 2017. 8. 20..
 */

public class Notify {
    public int num;
    @SerializedName("cover_num")
    public int coverNum;
    @SerializedName("comment_num")
    public int commentNum;
    @SerializedName("notify_type")
    public int notifyType;
    @SerializedName("my_nick")
    public String targetId;
    public String nickname;
    @SerializedName("notify_time")
    public String postdate;
    @SerializedName("profile_image")
    public String profileImage;
    @SerializedName("check_notify")
    public int check;
    @SerializedName("view_type")
    public int viewType;

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getCoverNum() {
        return coverNum;
    }

    public void setCoverNum(int coverNum) {
        this.coverNum = coverNum;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public int getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(int notifyType) {
        this.notifyType = notifyType;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPostdate() {
        return postdate;
    }

    public void setPostdate(String postdate) {
        this.postdate = postdate;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
