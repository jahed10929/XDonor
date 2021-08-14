package com.example.xdonor;

public class DonationViewModel {
    private String year, date, time, location, hospital, bloodgroup, lastDonation;

    public DonationViewModel(String year, String date, String time, String location, String hospital, String bloodgroup, String lastDonation) {
        this.year = year;
        this.date = date;
        this.time = time;
        this.location = location;
        this.hospital = hospital;
        this.bloodgroup = bloodgroup;
        this.lastDonation = lastDonation;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getBloodgroup() {
        return bloodgroup;
    }

    public void setBloodgroup(String bloodgroup) {
        this.bloodgroup = bloodgroup;
    }

    public String getLastDonation() {
        return lastDonation;
    }

    public void setLastDonation(String lastDonation) {
        this.lastDonation = lastDonation;
    }
}
