package com.ygaps.travelapp.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TourInfoResponse {
    @SerializedName("id")
    int mId;
    @SerializedName("hostId")
    String mHostId;
    @SerializedName("status")
    int mStatus;
    @SerializedName("name")
    String mName;
    @SerializedName("minCost")
    String mMinCost;
    @SerializedName("maxCost")
    String mMaxCost;
    @SerializedName("startDate")
    String mStartDate;
    @SerializedName("endDate")
    String mEndDate;
    @SerializedName("adults")
    int mAdults;
    @SerializedName("childs")
    int mChilds;
    @SerializedName("isPrivate")
    boolean mIsPrivate;
    @SerializedName("stopPoints")
    ArrayList<StopPointInfo> mStopPointList = new ArrayList<>();
    @SerializedName("comments")
    ArrayList<CommentInfo> mCommentList =new ArrayList<>();
    @SerializedName("members")
    ArrayList<MemberInfo> mMemberList =new ArrayList<>();

    public ArrayList<CommentInfo> getmCommentList() {
        return mCommentList;
    }

    public ArrayList<StopPointInfo> getmStopPointList() {
        return mStopPointList;
    }

    public int getmAdults() {
        return mAdults;
    }

    public ArrayList<MemberInfo> getmMemberList() {
        return mMemberList;
    }

    public int getmChilds() {
        return mChilds;
    }

    public int getmId() {
        return mId;
    }

    public int getmStatus() {
        return mStatus;
    }

    public String getmEndDate() {
        return mEndDate;
    }

    public String getmHostId() {
        return mHostId;
    }

    public String getmMaxCost() {
        return mMaxCost;
    }

    public String getmMinCost() {
        return mMinCost;
    }

    public String getmName() {
        return mName;
    }

    public String getmStartDate() {
        return mStartDate;
    }

    public boolean getmIsPrivate() {
        return mIsPrivate;
    }


    public void setmId(int mId) {
        this.mId = mId;
    }

    public void setmHostId(String mHostId) {
        this.mHostId = mHostId;
    }

    public void setmStatus(int mStatus) {
        this.mStatus = mStatus;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public void setmMinCost(String mMinCost) {
        this.mMinCost = mMinCost;
    }

    public void setmMaxCost(String mMaxCost) {
        this.mMaxCost = mMaxCost;
    }

    public void setmStartDate(String mStartDate) {
        this.mStartDate = mStartDate;
    }

    public void setmEndDate(String mEndDate) {
        this.mEndDate = mEndDate;
    }

    public void setmAdults(int mAdults) {
        this.mAdults = mAdults;
    }

    public void setmChilds(int mChilds) {
        this.mChilds = mChilds;
    }

    public void setmIsPrivate(boolean mIsPrivate) {
        this.mIsPrivate = mIsPrivate;
    }

    public void setmStopPointList(ArrayList<StopPointInfo> mStopPointList) {
        this.mStopPointList = mStopPointList;
    }

    public void setmCommentList(ArrayList<CommentInfo> mCommentList) {
        this.mCommentList = mCommentList;
    }

    public void setmMemberList(ArrayList<MemberInfo> mMemberList) {
        this.mMemberList = mMemberList;
    }
}
