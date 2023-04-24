package com.vriend.app;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.maps.model.LatLng;

public class Util {

    // Util method to hide the soft keyboard
    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    //takes in two longs and does a bit-wise AND, then finds degree of similarity
    public static int prefBitmaskComparison(long prefBMOne, long prefBMTwo)
    {
        long tempPrefBM = prefBMOne & prefBMTwo;
        int degreeOfSimilarity = Long.bitCount(tempPrefBM);
        return degreeOfSimilarity;
    }

    public static double distanceBetweenLatLng(double lat1, double lat2, double lng1, double lng2)
    {
        double distLat = lat2-lat1;
        double distLng = lng2-lng1;

        double dist = Math.sqrt((abs(pow(distLat,2)))*(abs(pow(distLng,2))));

        return dist;
    }
}
