package com.jyoung.honeystraw.model;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

/**
 * Created by jyoung on 2017. 8. 6..
 */
@Parcel
public class ReturnTips {
    public String returnImageUri;
    public String returnContent;
    public int returnStyleFlag;

    @ParcelConstructor
    public ReturnTips(String returnImageUri, String returnContent, int returnStyleFlag) {
        this.returnImageUri = returnImageUri;
        this.returnContent = returnContent;
        this.returnStyleFlag = returnStyleFlag;
    }

    public String getReturnImageUri() {
        return returnImageUri;
    }

    public void setReturnImageUri(String returnImageUri) {
        this.returnImageUri = returnImageUri;
    }

    public String getReturnContent() {
        return returnContent;
    }

    public void setReturnContent(String returnContent) {
        this.returnContent = returnContent;
    }

    public int getReturnStyleFlag() {
        return returnStyleFlag;
    }

    public void setReturnStyleFlag(int returnStyleFlag) {
        this.returnStyleFlag = returnStyleFlag;
    }
}
