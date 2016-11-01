package com.perfect_apps.koch.models;

/**
 * Created by mostafa_anter on 11/1/16.
 */

public class ClientInfo {
    private String id;
    private String name;
    private String email;
    private String mobile;
    private String image_full_path;

    public ClientInfo(String id, String name, String email, String mobile, String image_full_path) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.image_full_path = image_full_path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getImage_full_path() {
        return image_full_path;
    }

    public void setImage_full_path(String image_full_path) {
        this.image_full_path = image_full_path;
    }
}
