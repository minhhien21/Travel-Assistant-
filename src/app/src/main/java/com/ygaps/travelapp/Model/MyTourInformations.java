package com.ygaps.travelapp.Model;

public class MyTourInformations {
    private int id;
    private String hostId;
    private int status;
    private String name;
    private String minCost;
    private String maxCost;
    private String startDate;
    private String endDate;
    private String adults;
    private String childs;
    private String avatar;
    private Boolean isHost;
    private Boolean isKicked;

    public MyTourInformations(int id, String hostId, int status, String name, String minCost, String maxCost, String startDate, String endDate, String adults, String childs, String avatar, Boolean isHost, Boolean isKicked) {
        this.id = id;
        this.hostId = hostId;
        this.status = status;
        this.name = name;
        this.minCost = minCost;
        this.maxCost = maxCost;
        this.startDate = startDate;
        this.endDate = endDate;
        this.adults = adults;
        this.childs = childs;
        this.avatar = avatar;
        this.isHost = isHost;
        this.isKicked = isKicked;
    }

    public int getId() {
        return id;
    }

    public String getHostId() {
        return hostId;
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

    public String getAvatar() {
        return avatar;
    }

    public Boolean getIsHost() {
        return isHost;
    }

    public Boolean getIsKiched() {
        return isKicked;
    }
}
