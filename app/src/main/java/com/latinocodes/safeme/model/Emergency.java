package com.latinocodes.safeme.model;

public class Emergency {

    private String type;
    private String initiatorID;
    private Notification notification;

    public Emergency(){}

    public Emergency(String type, String initiatorID, Notification notification) {
        this.type = type;
        this.initiatorID = initiatorID;
        this.notification = notification;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInitiatorID() {
        return initiatorID;
    }

    public void setInitiatorID(String initiatorID) {
        this.initiatorID = initiatorID;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }
}
