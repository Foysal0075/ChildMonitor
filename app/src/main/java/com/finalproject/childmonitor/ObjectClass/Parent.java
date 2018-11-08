package com.finalproject.childmonitor.ObjectClass;

public class Parent {
    private String name;
    private String email;
    private String mobileNumber;

    public Parent(String name, String email, String mobileNumber) {
        this.name = name;
        this.email = email;
        this.mobileNumber = mobileNumber;
    }

    public Parent() {
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }
}
