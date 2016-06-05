package com.example.mrbank.blackboxdesign.Database.Models;

/**
 * Created by mrbank on 19/05/16.
 */
public class LocationDetails {

    private int id;
    private String locationName;
    private double latitude;
    private double longtitude;


    public LocationDetails(){}

    public LocationDetails(String locationName,double latitude,double longtitude){
        this.locationName = locationName;
        this.latitude = latitude;
        this.longtitude = longtitude;
    }

    public LocationDetails(int id,String locationName,double latitude,double longtitude){
        this.id = id;
        this.locationName = locationName;
        this.latitude = latitude;
        this.longtitude = longtitude;
    }

    public int getLocationId() {
        return id;
    }

    public String getLocationName() {
        return locationName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }


    public LocationDetails setId(int id) {
        this.id = id;
        return this;
    }

    public LocationDetails setLocationName(String locationName) {
        this.locationName = locationName;
        return this;
    }

    public LocationDetails setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public LocationDetails setLongtitude(double longtitude) {
        this.longtitude = longtitude;
        return this;
    }
}
