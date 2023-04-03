package com.vriend.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.vriend.app.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initProgressDialog();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Set click listener for Login Button
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If validation succeeds, proceed with the Firebase Login API call.
                if (validateFields()) {
                    login();
                }
            }
        });

        // Set click listener for SignUp Button
        binding.tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.hideKeyboard(LoginActivity.this, binding.getRoot());
                navigateToSignUpScreen();
            }
        });
    }

    /**
     * Method to initialize the loader.
     */
    private void initProgressDialog() {
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setIndeterminate(true);
    }

    /**
     * Method to validate input fields.
     */
    private boolean validateFields() {
        // If email is empty, show error message.
        if (binding.etEmail.getText().toString().isEmpty()) {
            Toast.makeText(LoginActivity.this, "Email Id cannot be empty!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        // If email id is not valid, show error message.
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.getText()).matches()) {
            Toast.makeText(LoginActivity.this, "Email Id is not valid!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        // If password is empty, show error message.
        if (binding.etPassword.getText().toString().isEmpty()) {
            Toast.makeText(LoginActivity.this, "Password cannot be empty!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Method for the Firebase Login API.
     */
    private void login() {
        // Get the email id and password from the fields.
        String emailId = binding.etEmail.getText().toString();
        String password = binding.etPassword.getText().toString();

        progressDialog.show();

        // Firebase API call to sign in the user using email id and password.
        mAuth.signInWithEmailAndPassword(emailId, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        // If Firebase sends a Successful response, show a message and navigate to Home Screen
                        // else, show the error message from Firebase.
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Logged In successfully.",
                                    Toast.LENGTH_SHORT).show();
                            navigateToHomeScreen();
                        } else { Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in, navigate to home screen
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            navigateToHomeScreen();
        }
    }

    /**
     *  Method to navigate to the Home Screen
     */
    private void navigateToHomeScreen() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /**
     *  Method to navigate to the SignUp Screen
     */
    private void navigateToSignUpScreen() {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }
}