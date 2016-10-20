package com.perfect_apps.koch.models;

/**
 * Created by mostafa on 13/07/16.
 */
public class ConversationItem {
    public ConversationItem(String userAvatar, String message, boolean show, String timestamp, int group_id, String messageOwnerEmail, String user_id, String user_to_id) {
        this.userAvatar = userAvatar;
        this.message = message;
        this.show = show;
        this.timestamp = timestamp;
        this.group_id = group_id;
        this.messageOwnerEmail = messageOwnerEmail;
        this.user_id = user_id;
        this.user_to_id =user_to_id;
    }

    private String userAvatar;
    private String message;
    private boolean show;
    private String timestamp;
    private int group_id;
    private String messageOwnerEmail;

    public String getUser_to_id() {
        return user_to_id;
    }

    public void setUser_to_id(String user_to_id) {
        this.user_to_id = user_to_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    private String user_id;
    private String user_to_id;

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public String getMessageOwnerEmail() {
        return messageOwnerEmail;
    }

    public void setMessageOwnerEmail(String messageOwnerEmail) {
        this.messageOwnerEmail = messageOwnerEmail;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


}
