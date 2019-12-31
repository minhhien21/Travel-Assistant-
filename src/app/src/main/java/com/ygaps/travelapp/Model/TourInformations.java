package com.ygaps.travelapp.Model;


public class TourInformations {

    private int id;
    private int status;
    private String name;
    private String minCost;
    private String maxCost;
    private String startDate;
    private String endDate;
    private String adults;
    private String childs;
    private String isPrivate;
    private String avatar;

    public TourInformations(int id, int status, String name, String minCost, String maxCost, String startDate, String endDate, String adults, String childs, String isPrivate, String avatar) {
        this.id = id;
        this.status = status;
        this.name = name;
        this.minCost = minCost;
        this.maxCost = maxCost;
        this.startDate = startDate;
        this.endDate = endDate;
        this.adults = adults;
        this.childs = childs;
        this.isPrivate = isPrivate;
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public int getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public String getMinCost() {
        return minCost;
    }

    public String getMaxCost() {
        return maxCost;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getAdults() {
        return adults;
    }

    public String getChilds() {
        return childs;
    }

    public String isPrivate() {
        return isPrivate;
    }

    public String getAvatar() {
        return avatar;
    }
}
