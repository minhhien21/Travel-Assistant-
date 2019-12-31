package com.ygaps.travelapp.Model;

import com.google.gson.annotations.SerializedName;

public class CommentInfo {
    @SerializedName("id")
    int mId;
    @SerializedName("name")
    String mName;
    @SerializedName("comment")
    String mComment;
    @SerializedName("avatar")
    String mAvatar;

    public CommentInfo(int aId, String aName, String aComment, String aAvatar)
    {
        mId = aId;
        mName = aName;
        mComment = aComment;
        mAvatar = "";
    }

    public String getmName() {
        return mName;
    }

    public int getmId() {
        return mId;
    }

    public String getmAvatar() {
        return mAvatar;
    }

    public String getmComment() {
        return mComment;
    }
}
