package com.jyoung.honeystraw.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jyoung on 2017. 8. 1..
 */
public class TipTemplate implements Parcelable {

    public TipTemplate(String tipImage, String tipContent, int tipTemplateNum) {
        this.tipImage = tipImage;
        this.tipContent = tipContent;
        this.tipTemplateNum = tipTemplateNum;
    }

    public int getTipTemplateNum() {
        return tipTemplateNum;
    }

    public void setTipTemplateNum(int tipTemplateNum) {
        this.tipTemplateNum = tipTemplateNum;
    }

    public String getTipImage() {
        return tipImage;
    }

    public void setTipImage(String tipImage) {
        this.tipImage = tipImage;
    }

    public String getTipContent() {
        return tipContent;
    }

    public void setTipContent(String tipContent) {
        this.tipContent = tipContent;
    }

    private String tipImage;
    private String tipContent;
    private int tipTemplateNum;

    public TipTemplate(Parcel in) {
        readFromParcel(in);
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(tipImage);
        parcel.writeString(tipContent);
        parcel.writeInt(tipTemplateNum);
    }
    private void readFromParcel(Parcel in){
        tipImage = in.readString();
        tipContent = in.readString();
        tipTemplateNum = in.readInt();

    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public TipTemplate createFromParcel(Parcel in) {
            return new TipTemplate(in);
        }

        public TipTemplate[] newArray(int size) {
            return new TipTemplate[size];
        }
    };
}
