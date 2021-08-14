package com.example.xdonor;

public class ActionLogModel {
    private String date;
    private String time;
    private String action;

    public ActionLogModel(String date, String time, String action) {
        this.date = date;
        this.time = time;
        this.action = action;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
