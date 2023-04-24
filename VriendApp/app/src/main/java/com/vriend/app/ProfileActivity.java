package com.vriend.app;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ToggleButton;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vriend.app.databinding.ActivityProfileBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.function.Consumer;


public class ProfileActivity extends AppCompatActivity {

    //'globals' needed to extract data from lambdas
    public LatLng thisLocation;
    public long preferenceBitmask = 0;

    //binding object
    private ActivityProfileBinding binding;

    //auth for db read-in
    private FirebaseAuth mAuth;

    //button objects
    private ToggleButton Event1, Event2, Event3, Event4, Event5, Event6, Event7, Event8, Event9, Event10, Event11, Event12, Event13, Event14, Event15, Event16, Event17;

    //this function is called when exiting the activity. It pushes the latest version of the bitmask to the DB, as well as the LatLng and time

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void updatePreferences(boolean isHost) {
        //temp value to hold long bitmask for storage into DB

        //fetching buttons
        Event1 = (ToggleButton) findViewById(R.id.Event1);
        Event2 = (ToggleButton) findViewById(R.id.Event2);
        Event3 = (ToggleButton) findViewById(R.id.Event3);
        Event4 = (ToggleButton) findViewById(R.id.Event4);
        Event5 = (ToggleButton) findViewById(R.id.Event5);
        Event6 = (ToggleButton) findViewById(R.id.Event6);
        Event7 = (ToggleButton) findViewById(R.id.Event7);
        Event8 = (ToggleButton) findViewById(R.id.Event8);
        Event9 = (ToggleButton) findViewById(R.id.Event9);
        Event10 = (ToggleButton) findViewById(R.id.Event10);
        Event11 = (ToggleButton) findViewById(R.id.Event11);
        Event12 = (ToggleButton) findViewById(R.id.Event12);
        Event13 = (ToggleButton) findViewById(R.id.Event13);
        Event14 = (ToggleButton) findViewById(R.id.Event14);
        Event15 = (ToggleButton) findViewById(R.id.Event15);
        Event16 = (ToggleButton) findViewById(R.id.Event16);
        Event17 = (ToggleButton) findViewById(R.id.Event17);


        //making comparisons
        if (Event1.isChecked()) {
            preferenceBitmask = preferenceBitmask | 1;
        }
        if (Event2.isChecked()) {
            preferenceBitmask = preferenceBitmask | 2;
        }
        if (Event3.isChecked()) {
            preferenceBitmask = preferenceBitmask | 4;
        }
        if (Event4.isChecked()) {
            preferenceBitmask = preferenceBitmask | 8;
        }
        if (Event5.isChecked()) {
            preferenceBitmask = preferenceBitmask | 16;
        }
        if (Event6.isChecked()) {
            preferenceBitmask = preferenceBitmask | 32;
        }
        if (Event7.isChecked()) {
            preferenceBitmask = preferenceBitmask | 64;
        }
        if (Event8.isChecked()) {
            preferenceBitmask = preferenceBitmask | 128;
        }
        if (Event9.isChecked()) {
            preferenceBitmask = preferenceBitmask | 256;
        }
        if (Event10.isChecked()) {
            preferenceBitmask = preferenceBitmask | 512;
        }
        if (Event11.isChecked()) {
            preferenceBitmask = preferenceBitmask | 1024;
        }
        if (Event12.isChecked()) {
            preferenceBitmask = preferenceBitmask | 2048;
        }
        if (Event13.isChecked()) {
            preferenceBitmask = preferenceBitmask | 4096;
        }
        if (Event14.isChecked()) {
            preferenceBitmask = preferenceBitmask | 8192;
        }
        if (Event15.isChecked()) {
            preferenceBitmask = preferenceBitmask | 16384;
        }
        if (Event16.isChecked()) {
            preferenceBitmask = preferenceBitmask | 32768;
        }
        if (Event17.isChecked()) {
            preferenceBitmask = preferenceBitmask | 65536;
        }


        //fetching latlng
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.w("perms","No Perms for geolocation, requesting");


            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},200);
            Log.i("perms","Perms successfully requested");

        }

        Executor myExecutor = this.getApplicationContext().getMainExecutor();

        Log.i("status","Executor generated, provider attached");


        lm.getCurrentLocation(
                LocationManager.GPS_PROVIDER,
                null,
                myExecutor,
                new Consumer<Location>() {
                    @Override
                    public void accept(Location location) {
                        double currlongitude = location.getLongitude();
                        double currlatitude = location.getLatitude();

                        thisLocation = new LatLng(currlatitude,currlongitude);
                        Log.i("status","Location received, reading lat = " + thisLocation.latitude + " lng = " + thisLocation.longitude);

                        //fetching time for timestamp
                        String currentTime = Instant.now().toString();

                        //reading data into DB
                        DatabaseReference mDatabase;
                        FirebaseUser user = mAuth.getCurrentUser();
                        String uID = user.getUid();
                        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uID);

                        //creating and populating userInfo poco
                        userInfo tempUser = new userInfo(preferenceBitmask,thisLocation,isHost,currentTime);

                        //pushing temp poco to DB
                        mDatabase.setValue(tempUser);
                    }
                });

        //remnants of hell
        //Location location = lm.getCurrentLocation(myVar,null,myExecutor,null);

        //Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        //double currlongitude = location.getLongitude();
        //double currlatitude = location.getLatitude();


        //LatLng thisLocation = new LatLng(currlatitude,currlongitude);


    }

    //this function is called on view creation and sets the toggles based on the current bitmask
    public void updateToggleSwitches()
    {
        Log.i("status","Beginning data read-in");
        DatabaseReference userDatabase;
        FirebaseUser user = mAuth.getCurrentUser();
        String uID = user.getUid();
        userDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        Log.i("status","Database connection estab, reading");

        userDatabase.child(uID).child("prefBitmask").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>(){

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Log.d("firebase", "prefBitmask = " + String.valueOf(task.getResult().getValue()));

                if(task.getResult().getValue() == null)
                {
                    return;
                }

                long tempBitmask = (long) task.getResult().getValue();

                //fetching buttons (need to refetch due to lambda scope restrictions)
                Event1 = (ToggleButton) findViewById(R.id.Event1);
                Event2 = (ToggleButton) findViewById(R.id.Event2);
                Event3 = (ToggleButton) findViewById(R.id.Event3);
                Event4 = (ToggleButton) findViewById(R.id.Event4);
                Event5 = (ToggleButton) findViewById(R.id.Event5);
                Event6 = (ToggleButton) findViewById(R.id.Event6);
                Event7 = (ToggleButton) findViewById(R.id.Event7);
                Event8 = (ToggleButton) findViewById(R.id.Event8);
                Event9 = (ToggleButton) findViewById(R.id.Event9);
                Event10 = (ToggleButton) findViewById(R.id.Event10);
                Event11 = (ToggleButton) findViewById(R.id.Event11);
                Event12 = (ToggleButton) findViewById(R.id.Event12);
                Event13 = (ToggleButton) findViewById(R.id.Event13);
                Event14 = (ToggleButton) findViewById(R.id.Event14);
                Event15 = (ToggleButton) findViewById(R.id.Event15);
                Event16 = (ToggleButton) findViewById(R.id.Event16);
                Event17 = (ToggleButton) findViewById(R.id.Event17);

                if ((tempBitmask & 1) != 0) {
                    Event1.setChecked(true);
                }
                if ((tempBitmask & 2) != 0) {
                    Event2.setChecked(true);
                }
                if ((tempBitmask & 4) != 0) {
                    Event3.setChecked(true);
                }
                if ((tempBitmask & 8) != 0) {
                    Event4.setChecked(true);
                }
                if ((tempBitmask & 16) != 0) {
                    Event5.setChecked(true);
                }
                if ((tempBitmask & 32) != 0) {
                    Event6.setChecked(true);
                }
                if ((tempBitmask & 64) != 0) {
                    Event7.setChecked(true);
                }
                if ((tempBitmask & 128) != 0) {
                    Event8.setChecked(true);
                }
                if ((tempBitmask & 256) != 0) {
                    Event9.setChecked(true);
                }
                if ((tempBitmask & 512) != 0) {
                    Event10.setChecked(true);
                }
                if ((tempBitmask & 1024) != 0) {
                    Event11.setChecked(true);
                }
                if ((tempBitmask & 2048) != 0) {
                    Event12.setChecked(true);
                }
                if ((tempBitmask & 4096) != 0) {
                    Event13.setChecked(true);
                }
                if ((tempBitmask & 8192) != 0) {
                    Event14.setChecked(true);
                }
                if ((tempBitmask & 16384) != 0) {
                    Event15.setChecked(true);
                }
                if ((tempBitmask & 32768) != 0) {
                    Event16.setChecked(true);
                }
                if ((tempBitmask & 65536) != 0) {
                    Event17.setChecked(true);
                }

            }
        });


        //old implementation
        /*try {
            userDatabase.orderByKey().equalTo(uID).addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    userInfo tempUser = snapshot.getValue(userInfo.class);
                    Log.i("status", "Snapshot retrieved, object = " + tempUser);
                    long tempBitmask = tempUser.prefBitmask;
                    Log.i("status", "bitmask set at = " + tempUser.prefBitmask);

                    if ((tempBitmask & 1) == 1) {
                        Event1.setChecked(true);
                    }
                    if ((tempBitmask & 2) == 1) {
                        Event2.setChecked(true);
                    }
                    if ((tempBitmask & 4) == 1) {
                        Event3.setChecked(true);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    //shouldn't be called basically ever, but will log anyway
                    Log.w("status", "database query cancelled due to error");
                }
            });
        }
        catch(Exception e)
        {
            Log.w("status","Retrieval failed with msg " + e);
        }*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //setting mAuth perms and ref
        mAuth = FirebaseAuth.getInstance();

        //update the toggles
        updateToggleSwitches();

        //btn control for home transition

        binding.btnHome.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public  void onClick(View view) {
                //update pref
                updatePreferences(false);

                //navigate to home page
                ProfileActivity.this.finish();
                Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        //btn control for maps/main activity transition

        binding.btnFindEvent.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public  void onClick(View view) {
                //update pref
                updatePreferences(false);

                //navigate to maps/activity page
                ProfileActivity.this.finish();
                Intent intent = new Intent(ProfileActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        //btn control for hosting event
        binding.btnHostEvent.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public  void onClick(View view) {
                //update pref
                updatePreferences(true);

                //navigate to maps/activity page
                ProfileActivity.this.finish();
                Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}