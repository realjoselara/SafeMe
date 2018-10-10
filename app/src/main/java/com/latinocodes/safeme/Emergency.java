package com.latinocodes.safeme;

public class Emergency {
    private String type;
    private String text;
//    User userdata;
    public Emergency(String type, String uid){
        this.type = type;
        getUser(uid);
    }

    public boolean sendAlert(){return true;}
    public void setText(){}
    public String getText(){return this.text;}
    public String getType(){return this.type;}
    public boolean sendreport(){return true;}
    public void getUser(String userId){}
}
