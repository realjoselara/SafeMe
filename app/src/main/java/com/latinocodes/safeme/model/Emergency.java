package com.latinocodes.safeme.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Emergency {

    private String type;
    private String initiatorID;
    private Notification notification;
    private String date;

    public Emergency(){}

    public Emergency(String type, String initiatorID, Notification notification) {
        this.type = type;
        this.initiatorID = initiatorID;
        this.notification = notification;
        this.date = setDate();
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

    public String setDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);

    }

    public String getDate(){return date;}
}
