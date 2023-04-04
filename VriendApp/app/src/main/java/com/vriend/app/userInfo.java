package com.vriend.app;

import com.google.android.gms.maps.model.LatLng;

public class userInfo {
    //need: LatLng, time (TBI), user pref bitmask

    //public LatLng position;
    public long prefBitmask;

    public userInfo() {
        //default constructor
    }

    public userInfo(long prefBitmask){
        //this.position = position;
        this.prefBitmask = prefBitmask;
    }

}
