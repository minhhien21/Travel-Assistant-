package com.ygaps.travelapp.Model;

import com.google.gson.annotations.SerializedName;

public class StopPointInfo {
    @SerializedName("id")
    int mId;
    @SerializedName("serviceId")
    int serviceId;
    @SerializedName( "address")
    private String mAddress;
    @SerializedName("name")
    String mName;
    @SerializedName("provinceId")
    int mProvinceId;
    @SerializedName("lat")
    String mLat;
    @SerializedName("long")
    String mLong;
    @SerializedName("arrivalAt")
    String mArrivalAt;
    @SerializedName("leaveAt")
    String mLeaveAt;
    @SerializedName("minCost")
    String mMincost;
    @SerializedName("maxCost")
    String mMaxCost;
    @SerializedName("serviceTypeId")
    int mServiceTypeId;
    @SerializedName("avatar")
    private String mAvatar;

    public StopPointInfo(int mId, int serviceId, String mAddress, String mName, int mProvinceId, String mLat, String mLong, String mArrivalAt, String mLeaveAt, String mMincost, String mMaxCost, int mServiceTypeId, String mAvatar) {
        this.mId = mId;
        this.serviceId = serviceId;
        this.mAddress = mAddress;
        this.mName = mName;
        this.mProvinceId = mProvinceId;
        this.mLat = mLat;
        this.mLong = mLong;
        this.mArrivalAt = mArrivalAt;
        this.mLeaveAt = mLeaveAt;
        this.mMincost = mMincost;
        this.mMaxCost = mMaxCost;
        this.mServiceTypeId = mServiceTypeId;
        this.mAvatar = mAvatar;
    }

    public int getmProvinceId() {
        return mProvinceId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public String getmAddress() {
        return mAddress;
    }

    public String getmAvatar() {
        return mAvatar;
    }

    public int getmId() {
        return mId;
    }

    public String getmName() {
        return mName;
    }

    public String getmMaxCost() {
        return mMaxCost;
    }

    public int getmServiceTypeId() {
        return mServiceTypeId;
    }

    public String getmArrivalAt() {
        return mArrivalAt;
    }

    public String getmLat() {
        return mLat;
    }

    public String getmLeaveAt() {
        return mLeaveAt;
    }

    public String getmLong() {
        return mLong;
    }

    public String getmMincost() {
        return mMincost;
    }
}