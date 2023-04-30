package com.example.cryptowatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {
    EditText emailEditText;
    EditText passwordEditText;
    Button saveButton;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private NotificationHandler notificationHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        int secret_key = getIntent().getIntExtra("SECRET_KEY",0);
        if (secret_key != 20000908){finish();}

        mAuth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) finish();

        emailEditText = findViewById(R.id.editTextProfileEmailAddress);
        emailEditText.setText(currentUser.getEmail());

        passwordEditText = findViewById(R.id.editTextProfileChangePassword);

        saveButton = findViewById(R.id.saveProfileButton);
        Animation animation = AnimationUtils.loadAnimation(ProfileActivity.this, R.anim.righttoleft);
        saveButton.startAnimation(animation);

        notificationHandler = new NotificationHandler(this);

    }

    public void deleteProfile(View view) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        currentUser.delete().addOnCompleteListener(this, task -> {
            if (task.isSuccessful()){
                Toast.makeText(ProfileActivity.this, "Your account has been deleted!" , Toast.LENGTH_LONG).show();
                notificationHandler.cancel();
                gotoLogin();
            } else {
                Toast.makeText(ProfileActivity.this, "Failed to delete your account!\n" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void gotoLogin(){
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void save(View view) {
        String newEmail = emailEditText.getText().toString();
        String newPassword = passwordEditText.getText().toString();

        if (!newEmail.trim().equals("") && !newEmail.equals(currentUser.getEmail())){
            if (currentUser == null) return;
            currentUser.updateEmail(newEmail).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, "Email address updated successfully.\nPlease sign in again", Toast.LENGTH_LONG).show();
                    mAuth.signOut();
                    finish();
                } else {
                    Toast.makeText(ProfileActivity.this, "Failed to update your email address.\n" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
        if (!newPassword.trim().equals("")){
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser == null) return;
            currentUser.updatePassword(newPassword).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, "Password updated successfully.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ProfileActivity.this, "Failed to update your password.\n" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) finish();
    }

    public void goBackToCrypto(View view) {
        finish();
    }

    public void logout(View view) {
        mAuth.signOut();
        finish();
    }
}