package com.example.cryptowatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.sql.Time;

public class RegisterActivity extends AppCompatActivity {
    private static final String PREF_KEY = LoginActivity.class.getPackage().toString();

    EditText usernameEditText;
    EditText emailEditText;
    EditText passwordEditText;
    EditText confirmPasswordEditText;
    Button registerButton;

    private SharedPreferences preferences;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        int secret_key = getIntent().getIntExtra("SECRET_KEY",0);
        if (secret_key != 20000908){finish();}

        usernameEditText = findViewById(R.id.editTextRegisterPersonName);
        emailEditText = findViewById(R.id.editTextRegisterEmailAddress);
        passwordEditText = findViewById(R.id.editTextRegisterPassword);
        confirmPasswordEditText = findViewById(R.id.editTextConfirmPassword);

        registerButton = findViewById(R.id.buttonRegister);
        Animation animation = AnimationUtils.loadAnimation(RegisterActivity.this, R.anim.lefttoright);
        registerButton.startAnimation(animation);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        String email = preferences.getString("email","");
        String password = preferences.getString("password","");
        emailEditText.setText(email);
        passwordEditText.setText(password);
        confirmPasswordEditText.setText(password);

        mAuth = FirebaseAuth.getInstance();
    }

    public void register(View view){
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
            Toast.makeText(RegisterActivity.this, "Please add all your informations!", Toast.LENGTH_LONG).show();
        }else if (!password.equals(confirmPassword)){
            Toast.makeText(RegisterActivity.this, "The passwords must be the same!", Toast.LENGTH_LONG).show();
        } else{
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "Account created successfully!", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Failed to create your account!\n" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }
    public void cancel(View view) {
        finish();
    }
}