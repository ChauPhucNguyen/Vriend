package com.vriend.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.vriend.app.databinding.ActivityProfileBinding;

public class ProfileActivity extends AppCompatActivity {

    //binding object
    private ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //btn control for home transition

        binding.btnHome.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View view) {
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
                //navigate to maps/activity page
                ProfileActivity.this.finish();
                Intent intent = new Intent(ProfileActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
    }


}