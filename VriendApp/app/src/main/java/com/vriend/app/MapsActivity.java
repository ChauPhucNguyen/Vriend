package com.vriend.app;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vriend.app.databinding.ActivityMapsBinding;
import com.vriend.app.userInfo;
import com.vriend.app.Util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private FirebaseAuth mAuth;


    //this function calls in the relevant data and provides a best match, then marks location for zooming
    public void findNearestEvent()
    {
        userInfo bestMatch;

        //db connection
        DatabaseReference userDatabase;
        FirebaseUser user = mAuth.getCurrentUser();
        String uID = user.getUid();
        userDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        //pull in this user's information
        userDatabase.child(uID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                // Log.d("firebase", "user object = " + String.valueOf(task.getResult()));

                final Map thisUser = (Map) task.getResult().getValue();
                Log.i("UserData","data read-in as " + thisUser.get("currPos"));

                //need new listener to get other user data
                userDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        //setup collection of candidate events
                        List<userInfo> viableEvents= new ArrayList<>();
                        Map targetUser;
                        boolean isViable;
                        Map posBeta = (Map)thisUser.get("currPos");
                        double lat2 = (double) posBeta.get("latitude");
                        double lng2 = (double) posBeta.get("longitude");
                        LatLng thisPos = new LatLng(lat2,lng2);

                        for(DataSnapshot taskChild: task.getResult().getChildren())
                        {
                            targetUser = (Map) taskChild.getValue();
                            isViable = true;
                            if((boolean)targetUser.get("isHost"))
                            {
                                isViable = false;
                            }

                            Map posAlpha = (Map)targetUser.get("currPos");
                            double lat1 = (double) posAlpha.get("latitude");
                            double lng1 = (double) posAlpha.get("longitude");

                            if(Util.distanceBetweenLatLng(lat1,lat2,lng1,lng2)>1)
                            {
                                isViable = false;
                            }

                            String tempTimeBuffer;
                            tempTimeBuffer = (String) thisUser.get("timeStamp");
                            Instant myTime = Instant.parse(tempTimeBuffer);

                            tempTimeBuffer = (String) targetUser.get("timeStamp");
                            Instant targetTime = Instant.parse(tempTimeBuffer);

                            long temporalDist;


                            if(myTime.compareTo(targetTime) > 0 )
                            {
                                temporalDist = targetTime.until(myTime, ChronoUnit.HOURS);
                            }
                            else
                            {
                                temporalDist = myTime.until(targetTime, ChronoUnit.HOURS);
                            }
                            if(temporalDist>6)
                            {
                                isViable = false;
                            }

                            if(isViable)
                            {
                                userInfo tempCandidate = new userInfo();
                                tempCandidate.prefBitmask = (long) targetUser.get("prefBitmask");
                                LatLng targetPos = new LatLng(lat1,lng1);
                                tempCandidate.setCurrPos(targetPos);
                                viableEvents.add(tempCandidate);
                            }


                        }
                        LatLng bestLoc = new LatLng(0, 0);
                        int currentBestSim = 0;
                        long selfPrefBitmask = (long) thisUser.get("prefBitmask");
                        for(userInfo targetCandidate : viableEvents)
                        {
                            mMap.addMarker(new MarkerOptions().alpha((float) 0.5).position(targetCandidate.currPos));
                            if(Util.prefBitmaskComparison(targetCandidate.prefBitmask, selfPrefBitmask) > currentBestSim)
                            {
                                currentBestSim = (Util.prefBitmaskComparison(targetCandidate.prefBitmask, selfPrefBitmask));
                                bestLoc = targetCandidate.currPos;

                            }
                        }
                        if(bestLoc.longitude==0 && bestLoc.latitude ==0)
                        {
                            mMap.addMarker(new MarkerOptions().position(thisPos).title("No Match"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(thisPos));
                        }
                        else {
                            mMap.addMarker(new MarkerOptions().position(bestLoc).title("Best Match"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(bestLoc));
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        mAuth = FirebaseAuth.getInstance();
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        findNearestEvent();

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}