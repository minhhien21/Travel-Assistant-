package com.ygaps.travelapp.Model;

import com.google.gson.annotations.SerializedName;

public class MemberInfo {
    @SerializedName("id")
    int mId;
    @SerializedName("name")
    String mName;
    @SerializedName("phone")
    String mPhone;
    @SerializedName("avatar")
    String mAvatar;
    @SerializedName("isHost")
    boolean isHost;

    public MemberInfo(int mId, String mName, String mPhone, String mAvatar, boolean isHost) {
        this.mId = mId;
        this.mName = mName;
        this.mPhone = mPhone;
        this.mAvatar = mAvatar;
        this.isHost = isHost;
    }

    public int getmId() {
        return mId;
    }

    public String getmName() {
        return mName;
    }

    public String getmPhone() {
        return mPhone;
    }

    public String getmAvatar() {
        return mAvatar;
    }

    public boolean isHost() {
        return isHost;
    }
}
