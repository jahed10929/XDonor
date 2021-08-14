package com.example.xdonor;

public class BloodReqiestViewModel {
    private String  image, name, bloodGroup,address, hospital, emergency;


    public BloodReqiestViewModel(String image,  String name, String bloodGroup, String address, String hospital, String emergency) {
        this.image = image;
        this.name = name;
        this.bloodGroup = bloodGroup;
        this.address = address;
        this.hospital = hospital;
        this.emergency = emergency;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getEmergency() {
        return emergency;
    }

    public void setEmergency(String emergency) {
        this.emergency = emergency;
    }
}
