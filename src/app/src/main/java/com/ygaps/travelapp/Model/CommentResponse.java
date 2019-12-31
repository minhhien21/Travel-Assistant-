package com.ygaps.travelapp.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CommentResponse {

    @SerializedName("commentList")
    ArrayList<CommentInfo> commentList;

    public ArrayList<CommentInfo> getCommentList() {
        return commentList;
    }
}