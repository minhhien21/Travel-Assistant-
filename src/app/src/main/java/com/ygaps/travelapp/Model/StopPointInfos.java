package com.ygaps.travelapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StopPointInfos {
    @SerializedName( "id")
    @Expose
    private Number id;
    @SerializedName( "serviceId")
    @Expose
    private Number serviceId;
    @SerializedName( "name")
    @Expose
    private String name;
    @SerializedName( "address")
    @Expose
    private String address;
    @SerializedName( "provinceId")
    @Expose
    private int provinceId;
    @SerializedName( "lat")
    @Expose
    private Double latitude;
    @SerializedName( "long")
    @Expose
    private Double longitude;
    @SerializedName( "arrivalAt")
    @Expose
    private long arrivalAt;
    @SerializedName( "leaveAt")
    @Expose
    private long leaveAt;
    @SerializedName( "serviceTypeId")
    @Expose
    private int serviceTypeId;
    @SerializedName("minCost")
    @Expose
    private String minCost;

    @SerializedName("maxCost")
    @Expose
    private String maxCost;

    public StopPointInfos(Number id, Number serviceId, String name, String address, int provinceId, Double latitude, Double longitude, long arrivalAt, long leaveAt, int serviceTypeId, String minCost, String maxCost) {
        this.id = id;
        this.serviceId = serviceId;
        this.name = name;
        this.address = address;
        this.provinceId = provinceId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.arrivalAt = arrivalAt;
        this.leaveAt = leaveAt;
        this.serviceTypeId = serviceTypeId;
        this.minCost = minCost;
        this.maxCost = maxCost;
    }

    public Number getId() {
        return id;
    }

    public Number getServiceId() {
        return serviceId;
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

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public long getArrivalAt() {
        return arrivalAt;
    }

    public long getLeaveAt() {
        return leaveAt;
    }

    public int getServiceTypeId() {
        return serviceTypeId;
    }

    public String getMinCost() {
        return minCost;
    }

    public String getMaxCost() {
        return maxCost;
    }

    public void setId(Number id) {
        this.id = id;
    }

    public void setServiceId(Number serviceId) {
        this.serviceId = serviceId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setArrivalAt(long arrivalAt) {
        this.arrivalAt = arrivalAt;
    }

    public void setLeaveAt(long leaveAt) {
        this.leaveAt = leaveAt;
    }

    public void setServiceTypeId(int serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public void setMinCost(String minCost) {
        this.minCost = minCost;
    }

    public void setMaxCost(String maxCost) {
        this.maxCost = maxCost;
    }
}
