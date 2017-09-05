package com.jyoung.honeystraw.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jyoung on 2017. 8. 30..
 */

public class CommentList{
    public int num;
    public String writer;
    public String content;
    public String postdate;
    @SerializedName("like_num")
    public int likeNum;
    @SerializedName(("cover_num"))
    public int coverNum;
    public String nickname;
    @SerializedName("profile_image")
    public String profileImage;
    @SerializedName("like_state")
    public int likeState;
    @SerializedName("comment_type")
    public int commentType;

    public CommentList() {
    }

    public int getCommentType() {
        return commentType;
    }

    public void setCommentType(int commentType) {
        this.commentType = commentType;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public int getLikeState() {
        return likeState;
    }

    public void setLikeState(int likeState) {
        this.likeState = likeState;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPostdate() {
        return postdate;
    }

    public void setPostdate(String postdate) {
        this.postdate = postdate;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public int getCoverNum() {
        return coverNum;
    }

    public void setCoverNum(int coverNum) {
        this.coverNum = coverNum;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

}