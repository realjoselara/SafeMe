package com.latinocodes.safeme.model;

public class User {

    private String userID;
    private String firstName;
    private String lastName;
    private String sex;
    private int age;
    private String locationCordinates;

    public User() {

    }

    public User(String userID, String firstName, String lastName, String sex, int age, String locationCordinates) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.sex = sex;
        this.age = age;
        this.locationCordinates = locationCordinates;
    }

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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getLocationCordinates() {
        return locationCordinates;
    }

    public void setLocationCordinates(String locationCordinates) {
        this.locationCordinates = locationCordinates;
    }
}
