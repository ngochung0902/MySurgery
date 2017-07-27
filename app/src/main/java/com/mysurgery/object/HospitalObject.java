package com.mysurgery.object;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 4/17/2017.
 */

public class HospitalObject {
    @SerializedName("_id")
    private int _id;
    @SerializedName("hospital")
    private String hospital;
    @SerializedName("appointment")
    private String[] appointment;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longitude")
    private String longitude;
    @SerializedName("contacts")
    private String contacts;
    @SerializedName("phone_number1")
    private String phone_number1;
    @SerializedName("phone_number2")
    private String phone_number2;
    @SerializedName("phone_number3")
    private String phone_number3;
    @SerializedName("type")
    private List<TypeObject> types;

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getContacts() {
        return contacts;
    }

    public void setPhone_number1(String phone_number1) {
        this.phone_number1 = phone_number1;
    }

    public String getPhone_number1() {
        return phone_number1;
    }

    public void setPhone_number2(String phone_number2) {
        this.phone_number2 = phone_number2;
    }

    public String getPhone_number2() {
        return phone_number2;
    }

    public void setPhone_number3(String phone_number3) {
        this.phone_number3 = phone_number3;
    }

    public String getPhone_number3() {
        return phone_number3;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getHospital() {
        return hospital;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int get_id() {
        return _id;
    }

    public void setAppointment(String[] appointment) {
        this.appointment = appointment;
    }

    public String[] getAppointment() {
        return appointment;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setTypes(List<TypeObject> types) {
        this.types = types;
    }

    public List<TypeObject> getTypes() {
        return types;
    }
}
