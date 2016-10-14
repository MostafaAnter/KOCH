package com.perfect_apps.koch.models;

/**
 * Created by mostafa_anter on 10/14/16.
 */

public class OrderItem {
    private String avatarUrl;
    private String userName;
    private String timestamp;

    public OrderItem(){

    }

    public OrderItem(String avatarUrl, String userName, String timestamp) {
        this.avatarUrl = avatarUrl;
        this.userName = userName;
        this.timestamp = timestamp;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
