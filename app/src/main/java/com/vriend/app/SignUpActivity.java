package com.vriend.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.vriend.app.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignupBinding binding;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initProgressDialog();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Set click listener for Sign Up Button
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If validation succeeds, proceed with the Firebase SignUp API call.
                if (validateFields()) {
                    Util.hideKeyboard(SignUpActivity.this, binding.getRoot());
                    signUp();
                }
            }
        });
    }

    /**
     * Method to initialize the loader.
     */
    private void initProgressDialog() {
        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setIndeterminate(true);
    }

    /**
     * Method to validate input fields.
     */
    private boolean validateFields() {
        // If First name is empty, show error message.
        if (binding.etFirstName.getText().toString().isEmpty()) {
            Toast.makeText(SignUpActivity.this, "First name cannot be empty!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        // If last name is empty, show error message.
        if (binding.etLastName.getText().toString().isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Last name cannot be empty!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        // If email id is empty, show error message.
        if (binding.etEmailId.getText().toString().isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Email Id cannot be empty!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        // If password is empty, show error message.
        if (binding.etPassword.getText().toString().isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Password cannot be empty!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        // If confirm password is empty, show error message.
        if (binding.etConfirmPassword.getText().toString().isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Confirm Password cannot be empty!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        // If password and confirm password doesn't match, show error message.
        if (!binding.etPassword.getText().toString().equals(binding.etConfirmPassword.getText().toString())) {
            Toast.makeText(SignUpActivity.this, "Passwords does not match!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        // If the Age confirmation checkbox is not checked by user, show error message.
        if (!binding.cbAge.isChecked()) {
            Toast.makeText(SignUpActivity.this, "Please confirm that you are at least 18 years of age!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        // if the above validations are done, return true which indicates that
        // we can proceed with the Firebase API call.
        return true;
    }

    /**
     * Method for the Firebase SignUp API.
     */
    private void signUp() {
        String emailId = binding.etEmailId.getText().toString();
        String password = binding.etPassword.getText().toString();

        progressDialog.show();

        // Firebase API call for creating user on Firebase
        mAuth.createUserWithEmailAndPassword(emailId, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If Firebase sends a Successful response, update the user's display name
                        // else, show the error message from Firebase.
                        if (task.isSuccessful()) {
                            setAdditionalData();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(SignUpActivity.this, task.getException().getLocalizedMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     *  Method to set the Display Name in the Firebase for the user, using the first name & last name input fields
     *  once the user is signed up successfully.
     */
    private void setAdditionalData() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String displayName = binding.etFirstName.getText().toString() + " "
                    + binding.etLastName.getText().toString();

            // Firebase API call to update the display name
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .build();
            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            // If Firebase sends a successful response, show the message and navigate to the Home Screen.
                            // else, show the error message.
                            if (task.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this, "Logged In successfully.",
                                        Toast.LENGTH_SHORT).show();
                                navigateToHomeScreen();
                            } else {
                                Toast.makeText(SignUpActivity.this, task.getException().getLocalizedMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            progressDialog.dismiss();
        }
    }

    /**
     *  Method to navigate to the Home Screen using flag to clear the previously opened activities.
     */
    private void navigateToHomeScreen() {
        SignUpActivity.this.finish();
        Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}