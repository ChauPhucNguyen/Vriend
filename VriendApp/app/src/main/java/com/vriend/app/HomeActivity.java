package com.vriend.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.vriend.app.databinding.ActivityHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Click listener for the Logout Button
        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // SignOut the user, finish the Home Screen Activity
                // and navigate back to the Login Screen.
                mAuth.signOut();
                HomeActivity.this.finish();
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // Click listener for profile transition
        binding.btnProfile.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View view) {
                //navigate to profile page
                HomeActivity.this.finish();
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        // Click listener for map/main page transition
        binding.btnMap.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View view) {
                //navigate to profile page
                HomeActivity.this.finish();
                Intent intent = new Intent(HomeActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in, get the display name of logged in user and show the greeting.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            binding.tvGreeting.setText("Welcome \n" + currentUser.getDisplayName());
        }
    }
}