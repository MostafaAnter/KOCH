package com.perfect_apps.koch.services;

/**
 * Created by mostafa_anter on 10/5/16.
 */

public class UpdateMessageCountEvent {
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;

    public UpdateMessageCountEvent(String message) {
        this.message = message;
    }
}
