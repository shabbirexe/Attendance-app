package com.example.attendanceapp;


public class Data {
    private String name;
    private String rollNumber;
    private int present;
    private int classes;

    public int getClasses() {
        return classes;
    }

    public String getName() {
        return name;
    }

    public void setClasses(int classes) {
        this.classes = classes;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public int getPresent() {
        return present;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public void setPresent(int present) {
        this.present = present;
    }
}

