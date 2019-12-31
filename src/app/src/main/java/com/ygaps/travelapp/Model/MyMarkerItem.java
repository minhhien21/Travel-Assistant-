package com.ygaps.travelapp.Model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MyMarkerItem implements ClusterItem {
    private final LatLng mPosition;
    private final String mTitle;
    private final String mSnippet;

    private final int infoPos;
    public MyMarkerItem(double lat, double lng, int infoPos, String mTitle, String mSnippet) {
        this.mPosition = new LatLng(lat, lng);
        this.mTitle = mTitle;
        this.mSnippet = mSnippet;
        this.infoPos = infoPos;

    }

    public MyMarkerItem(double lat, double lng, int infoPos) {
        this.mPosition = new LatLng(lat, lng);
        this.mTitle = "";
        this.mSnippet = "";
        this.infoPos = infoPos;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return mSnippet;
    }

    public  int getInfoPos(){
        return infoPos;
    }
}
