package com.jyoung.honeystraw.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jyoung on 2017. 8. 1..
 */

public class Comment {
    public int num;
    public String writer;
    public int likeNum;
    public int coverNum;
    public String postdate;
    public String content;


    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
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

    public String getPostdate() {
        return postdate;
    }

    public void setPostdate(String postdate) {
        this.postdate = postdate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
