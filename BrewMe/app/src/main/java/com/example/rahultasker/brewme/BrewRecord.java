package com.example.rahultasker.brewme;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class BrewRecord implements Serializable {
    @PrimaryKey(autoGenerate=true)
    public int id;

    @ColumnInfo
    public String name;

    @ColumnInfo
    public String type;

    @ColumnInfo
    public String street;

    @ColumnInfo
    public String city;

    @ColumnInfo
    public String state;

    @ColumnInfo
    public String country;

    @ColumnInfo
    public String postal_code;

    @ColumnInfo
    public String longitude;

    @ColumnInfo
    public String latitude;

    @ColumnInfo
    public String phone;

    @ColumnInfo
    public String website;

    @ColumnInfo
    public int brewID;

    public BrewRecord() {}

    public int getId() {
        return id;
    }

    public void setId(int type) {
        id = type;
    }

    public int getBrewId() {
        return id;
    }

    public void setBrewId(int type) {
        id = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String type) {
        this.name = type;
    }

    public String getBrewery_type() {
        return type;
    }

    public void setBrewery_type(String type) {
        this.type = type;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String type) {
        this.street = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String type) {
        this.state = type;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String type) {
        this.city = type;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String type) {
        this.postal_code = type;
    }
    public String getCountry() {
        return country;
    }

    public void setCountry(String type) {
        this.country = type;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String type) {
        this.longitude = type;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String type) {
        this.latitude = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String type) {
        this.phone = type;
    }

    public String getWebsite_url() {
        return website;
    }

    public void setWebsite_url(String type) {
        this.website = type;
    }
}
