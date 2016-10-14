package com.perfect_apps.koch.models;

/**
 * Created by mostafa_anter on 10/14/16.
 */

public class InboxItem {
    private String avatarUrl;
    private String userName;
    private String message;
    private String timestamp;
    private boolean seenFlag;

    public InboxItem(){

    }

    public InboxItem(String avatarUrl, String userName, String message, String timestamp, boolean seenFlag) {
        this.avatarUrl = avatarUrl;
        this.userName = userName;
        this.message = message;
        this.timestamp = timestamp;
        this.seenFlag = seenFlag;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSeenFlag() {
        return seenFlag;
    }

    public void setSeenFlag(boolean seenFlag) {
        this.seenFlag = seenFlag;
    }
}
