package com.jyoung.honeystraw.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jyoung on 2017. 8. 15..
 */
public class User implements Parcelable {
    public String id;
    public String nickname;
    @SerializedName("profile_image")
    public String profileImage;
    @SerializedName("state_message")
    public String stateMessage;
    public String password;
    @SerializedName("straw_num")
    public int strawCount;
    @SerializedName("like_num")
    public int likeCount;
    @SerializedName("tip_num")
    public int tipCount;

    public int getTipCount() {
        return tipCount;
    }

    public void setTipCount(int tipCount) {
        this.tipCount = tipCount;
    }

    public static Creator getCREATOR() {
        return CREATOR;
    }

    public int getStrawCount() {
        return strawCount;
    }

    public void setStrawCount(int strawCount) {
        this.strawCount = strawCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getStateMessage() {
        return stateMessage;
    }

    public void setStateMessage(String stateMessage) {
        this.stateMessage = stateMessage;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User() {
    }

    public User(Parcel in) {
        readFromParcel(in);
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(nickname);
        parcel.writeString(profileImage);
        parcel.writeString(stateMessage);
        parcel.writeString(password);
    }


    private void readFromParcel(Parcel in){
        id = in.readString();
        nickname = in.readString();
        profileImage = in.readString();
        stateMessage = in.readString();
        password = in.readString();

    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
