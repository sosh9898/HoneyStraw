package com.jyoung.honeystraw.base;

/**
 * Created by jyoung on 2017. 8. 1..
 */

public class BaseModel {
    public int viewType;
    public String nickName;

    public BaseModel(int viewType, String nickName) {
        this.viewType = viewType;
        this.nickName = nickName;
    }

    public int getViewType() {
        return viewType;
    }

    public String getNickName() {
        return nickName;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

}
