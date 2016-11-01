package com.perfect_apps.koch.models;

/**
 * Created by mostafa_anter on 10/14/16.
 */

public class InboxItem {
    private String chats_name;
    private String chat_id;
    private String user_id;
    private String chats_last_message;
    private String time_stamp;
    private String chats_avatar;
    private String new_count;
    private String group_id;

    public String getChats_name() {
        return chats_name;
    }

    public void setChats_name(String chats_name) {
        this.chats_name = chats_name;
    }

    public String getChat_id() {
        return chat_id;
    }

    public void setChat_id(String chat_id) {
        this.chat_id = chat_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getChats_last_message() {
        return chats_last_message;
    }

    public void setChats_last_message(String chats_last_message) {
        this.chats_last_message = chats_last_message;
    }

    public String getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(String time_stamp) {
        this.time_stamp = time_stamp;
    }

    public String getChats_avatar() {
        return chats_avatar;
    }

    public void setChats_avatar(String chats_avatar) {
        this.chats_avatar = chats_avatar;
    }

    public String getNew_count() {
        return new_count;
    }

    public void setNew_count(String new_count) {
        this.new_count = new_count;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public InboxItem(String chats_name, String chat_id, String user_id, String chats_last_message, String time_stamp, String chats_avatar, String new_count, String group_id) {
        this.chats_name = chats_name;
        this.chat_id = chat_id;
        this.user_id = user_id;
        this.chats_last_message = chats_last_message;
        this.time_stamp = time_stamp;
        this.chats_avatar = chats_avatar;
        this.new_count = new_count;
        this.group_id = group_id;
    }
}
