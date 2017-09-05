package com.jyoung.honeystraw.model;

/**
 * Created by jyoung on 2017. 8. 5..
 */

public class RegisterTip {
    int viewType;
    String tipImage;
    String tipText;

    public RegisterTip(int viewType, String tipImage, String tipText) {
        this.viewType = viewType;
        this.tipImage = tipImage;
        this.tipText = tipText;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getTipImage() {
        return tipImage;
    }

    public void setTipImage(String tipImage) {
        this.tipImage = tipImage;
    }

    public String getTipText() {
        return tipText;
    }

    public void setTipText(String tipText) {
        this.tipText = tipText;
    }
}
