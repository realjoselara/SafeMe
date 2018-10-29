package com.latinocodes.safeme.model;

import java.util.ArrayList;

public class User {

    private String userID;
    private String firstName;
    private String lastName;
    private String sex;
    private String age;
    private ArrayList<Double> locationCordinates;
    private String ethnicity;
//    private bitmap<bitmap> image;
    String TAG = "User";



    public User() {

    }

    public User(String uuid, String firstName, String lastName, String sex, String age, ArrayList<Double> location, String ethnicity){
        this.userID = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.sex = sex;
        this.age = age;
        this.locationCordinates = location;
        this.ethnicity = ethnicity;

    }


    public String getEthnicity() { return ethnicity; }

    public void setEthnicity(String ethnicity) { this.ethnicity = ethnicity; }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public ArrayList<Double> getLocationCordinates() {
        return locationCordinates;
    }

    public void setLocationCordinates(ArrayList<Double> locationCordinates) { this.locationCordinates = locationCordinates; }

}
