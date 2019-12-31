package com.ygaps.travelapp.Model;

public class MessageSend {
    private String comment; // message body
    private String avatar;
    private String name;
    private boolean isRecord = false;
    private int userId;
    private boolean belongsToCurrentUser; // is this message sent by us?


    public MessageSend(String comment, String avatar, String name, int userId, boolean belongsToCurrentUser) {
        this.comment = comment;
        this.avatar = avatar;
        this.name = name;
        this.userId = userId;
        this.belongsToCurrentUser = belongsToCurrentUser;
    }
    public MessageSend(String comment, String avatar, String name, int userId, boolean belongsToCurrentUser, boolean isRecord ) {
        this.comment = comment;
        this.avatar = avatar;
        this.name = name;
        this.userId = userId;
        this.belongsToCurrentUser = belongsToCurrentUser;
        this.isRecord = isRecord;
    }

    public boolean getIsRecord(){ return isRecord;}

    public String getComment() {
        return comment;
    }

    public void setComment(String text) {
        this.comment = text;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isBelongsToCurrentUser() {
        return belongsToCurrentUser;
    }

    public void setBelongsToCurrentUser(boolean belongsToCurrentUser) {
        this.belongsToCurrentUser = belongsToCurrentUser;
    }
}
