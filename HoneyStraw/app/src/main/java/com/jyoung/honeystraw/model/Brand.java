package com.jyoung.honeystraw.model;

import android.support.annotation.Nullable;

import com.jyoung.honeystraw.base.BaseModel;

/**
 * Created by jyoung on 2017. 8. 2..
 */

public class Brand extends BaseModel {
    String brandName;
    int logo;

    public Brand(int viewType, String nickName, String brandName, @Nullable int logo) {
        super(viewType, nickName);
        this.brandName = brandName;
        this.logo = logo;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public int getLogo() {
        return logo;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }
}
