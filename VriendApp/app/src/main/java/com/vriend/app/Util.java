package com.vriend.app;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Util {

    // Util method to hide the soft keyboard
    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    //takes in two longs and does a bit-wise AND, then finds degree of similarity
    public int prefBitmaskComparison(long prefBMOne, long prefBMTwo)
    {
        long tempPrefBM = prefBMOne & prefBMTwo;
        int degreeOfSimilarity = Long.bitCount(tempPrefBM);
        return degreeOfSimilarity;
    }

}
