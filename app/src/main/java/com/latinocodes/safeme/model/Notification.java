package com.latinocodes.safeme.model;

public class Notification {

    private String type;
    private String description;
    private boolean critical;
    String message;

    public Notification(String type, String description, boolean critical){
        this.type = type;
        this.description = description;
        this.critical = critical;
    }

    public void setMessage(){
        if (this.type.equals("Bomb")){
            this.message = "A Bomb has been detected near you.\n Please evacuate as soon as possible.";

        }else if(this.type.equals("Amber Alert") || this.type.equals("Other")){
            this.message = this.description;
        }else if(this.type.equals("Attack")){
            this.message = "Someone near you is under attack.";
        }else if(this.type.equals("Active Shooter")){
            this.message = "An active shooter has been reported near you. \n Please find cover.";
        }
    }

    public String getMessage(){
        return this.message;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCritical() {
        return critical;
    }

    public void setCritical(boolean critical) {
        this.critical = critical;
    }

    public void setType(String type) {
        this.type = type;
    }
}
