package com.example.mrbank.blackboxdesign.Database.Models;

/**
 * Created by mrbank on 19/05/16.
 */
public class LocationReports {

    private int id;
    private long startTimeInMiliSeconds;    // when the period started
    private long finishTimeInMiliSeconds;   // when the period finished
    private long durationInMiliSeconds;
    private int locationId;

    public LocationReports() {}

    public LocationReports(long startTimeInMiliSeconds,long finishTimeInMiliSeconds,int locationId) {
        this.startTimeInMiliSeconds = startTimeInMiliSeconds;
        this.finishTimeInMiliSeconds = finishTimeInMiliSeconds;
        durationInMiliSeconds = this.finishTimeInMiliSeconds - this.startTimeInMiliSeconds;
        this.locationId = locationId;
    }

    public LocationReports(int id,long startTimeInMiliSeconds,long finishTimeInMiliSeconds,int locationId) {
        this.id = id;
        this.startTimeInMiliSeconds = startTimeInMiliSeconds;
        this.finishTimeInMiliSeconds = finishTimeInMiliSeconds;
        durationInMiliSeconds = this.finishTimeInMiliSeconds - this.startTimeInMiliSeconds;
        this.locationId = locationId;
    }

    public LocationReports(long startTimeInMiliSeconds) {

        this.startTimeInMiliSeconds = startTimeInMiliSeconds;
    }




    public long getStartTimeInMiliSeconds() {
        return startTimeInMiliSeconds;
    }

    public long getFinishTimeInMiliSeconds() {
        return finishTimeInMiliSeconds;
    }

    public int getLocationId() {
        return locationId;
    }

    public int getId() {
        return id;
    }

    public LocationReports setId(int id) {
        this.id = id;
        return this;

    }

    public LocationReports setStartTimeInMiliSeconds(long startTimeInMiliSeconds) {
        this.startTimeInMiliSeconds = startTimeInMiliSeconds;
        return this;
    }

    public LocationReports setFinishTimeInMiliSeconds(long finishTimeInMiliSeconds) {
        this.finishTimeInMiliSeconds = finishTimeInMiliSeconds;
        return this;
    }

    public LocationReports setLocationId(int locationId) {
        this.locationId = locationId;
        return this;
    }

    public long getDurationInMiliSeconds() {
        return durationInMiliSeconds;
    }

    public LocationReports setDurationInMiliSeconds(long durationInMiliSeconds) {
        this.durationInMiliSeconds = durationInMiliSeconds;
        return this;
    }
}
