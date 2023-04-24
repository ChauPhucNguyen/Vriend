package com.vriend.app;

import com.google.android.gms.maps.model.LatLng;

import java.time.Instant;
import java.util.Date;

public class userInfo {

    //setters
    public void setPrefBitmask(long prefBitmask){
        this.prefBitmask = prefBitmask;
    }
    public void setCurrPos(LatLng currPos){
        this.currPos = currPos;
    }
    public void setIsHost(boolean isHost){
        this.isHost = isHost;
    }

    public void setTimeStamp(String timeStamp){
        this.timeStamp = timeStamp;
    }

    //getters
    public long getPrefBitmask(){
        return this.prefBitmask;
    }

    public String getTimeStamp(){
        return this.timeStamp;
    }

    public LatLng getCurrPos(){
        return this.currPos;
    }

    public boolean getIsHost(){
        return this.isHost;
    }

    //public LatLng position;
    public long prefBitmask;
    public LatLng currPos;
    public boolean isHost;

    public String timeStamp;

    public userInfo() {
        //default constructor
    }

    public userInfo(long prefBitmask, LatLng position, boolean isHost, String timeStamp){
        this.currPos = position;
        this.prefBitmask = prefBitmask;
        this.isHost = isHost;
        this.timeStamp = timeStamp;
    }

}
