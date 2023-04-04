package com.vriend.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vriend.app.databinding.ActivityProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ProfileActivity extends AppCompatActivity {

    //binding object
    private ActivityProfileBinding binding;

    //auth for db read-in
    private FirebaseAuth mAuth;

    //button objects
    private ToggleButton Event1, Event2, Event3;

    //this function is called when exiting the activity. It pushes the latest version of the bitmask to the DB, as well as the LatLng and time (TBI).
    public void updatePreferences()
    {
        //temp value to hold long bitmask for storage into DB
        long preferenceBitmask = 0;

        //fetching buttons
        Event1 = (ToggleButton) findViewById(R.id.Event1);
        Event2 = (ToggleButton) findViewById(R.id.Event2);
        Event3 = (ToggleButton) findViewById(R.id.Event3);

        //making comparisons
        if(Event1.isChecked()){
            preferenceBitmask = preferenceBitmask | 1;
        }
        if(Event2.isChecked()){
            preferenceBitmask = preferenceBitmask | 2;
        }
        if(Event3.isChecked()){
            preferenceBitmask = preferenceBitmask | 4;
        }

        //reading data into DB
        DatabaseReference mDatabase;
        FirebaseUser user = mAuth.getCurrentUser();
        String uID = user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uID);

        //creating and populating userInfo poco
        userInfo tempUser = new userInfo(preferenceBitmask);

        //pushing temp poco to DB
        mDatabase.setValue(tempUser);

    }

    //this function is called on view creation and sets the toggles based on the current bitmask
    public void updateToggleSwitches()
    {
        //TBI
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //setting mAuth perms and ref
        mAuth = FirebaseAuth.getInstance();

        //btn control for home transition

        binding.btnHome.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View view) {
                //update pref
                updatePreferences();

                //navigate to home page
                ProfileActivity.this.finish();
                Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        //btn control for maps/main activity transition

        binding.btnFindEvent.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View view) {
                //update pref
                updatePreferences();

                //navigate to maps/activity page
                ProfileActivity.this.finish();
                Intent intent = new Intent(ProfileActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
    }


}