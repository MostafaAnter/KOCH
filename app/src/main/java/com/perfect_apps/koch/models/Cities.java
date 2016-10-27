package com.perfect_apps.koch.models;

/**
 * Created by mostafa_anter on 10/26/16.
 */

public class Cities {
    private String id;
    private String name;

    public Cities(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
