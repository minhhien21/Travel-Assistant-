package com.ygaps.travelapp.Model;

public class SearchDestination {
    private int id;
    private String name;
    private String address;
    private int provinceId;
    private String mLat;
    private String mLong;
    private String minCost;
    private String maxCost;
    private String avatar;
    private int serviceTypeId;

    public SearchDestination(int id, String name, String address, int provinceId, String mLat, String mLong, String minCost, String maxCost, String avatar, int serviceTypeId) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.provinceId = provinceId;
        this.mLat = mLat;
        this.mLong = mLong;
        this.minCost = minCost;
        this.maxCost = maxCost;
        this.avatar = avatar;
        this.serviceTypeId = serviceTypeId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public String getmLat() {
        return mLat;
    }

    public String getmLong() {
        return mLong;
    }

    public String getMinCost() {
        return minCost;
    }

    public String getMaxCost() {
        return maxCost;
    }

    public String getAvatar() {
        return avatar;
    }

    public int getServiceTypeId() {
        return serviceTypeId;
    }
}
