package com.example.cinema;

public class Director {
    private String date;
    private String name;
    private String fullName;
    private String info;
    private String id;

    public String getId() {
        return id != null ? id : "No data";
    }

    public String getDate() {
        return date != null ? date : "No data";
    }

    public String getFullName() {
        return fullName != null ? fullName : "No data";
    }

    public String getInfo() {
        return info != null ? info : "No data";
    }

    public String getName() {
        return name != null ? name : "No data";
    }


    public void setDate(String date) {
        this.date = date;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setName(String name) {
        this.name = name;
    }

}
