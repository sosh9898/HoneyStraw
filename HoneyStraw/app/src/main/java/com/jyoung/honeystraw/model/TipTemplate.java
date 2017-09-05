package com.jyoung.honeystraw.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jyoung on 2017. 8. 1..
 */
public class TipTemplate implements Parcelable {

    public TipTemplate(String tipImage, String tipContent) {
        this.tipImage = tipImage;
        this.tipContent = tipContent;
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

    public String tipImage;
    public String tipContent;

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
    }
    private void readFromParcel(Parcel in){
        tipImage = in.readString();
        tipContent = in.readString();

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
