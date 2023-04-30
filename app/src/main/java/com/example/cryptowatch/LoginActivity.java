package com.example.cryptowatch;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private static final int SECRET_KEY = 20000908;
    private static final String PREF_KEY = LoginActivity.class.getPackage().toString();
    private static final String LOG_TAG = LoginActivity.class.getName();

    EditText emailEditText;
    EditText passwordEditText;
    Button loginButton;

    private SharedPreferences preferences;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        int secret_key = getIntent().getIntExtra("SECRET_KEY",0);
        if (secret_key != 20000908){finish();}

        emailEditText = findViewById(R.id.editTextLoginEmailAddress);
        passwordEditText = findViewById(R.id.editTextLoginPassword);
        loginButton = findViewById(R.id.buttonLogin);
        loginButton.setOnClickListener(v -> {
            Animation animation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.bounce);
            loginButton.startAnimation(animation);
            login(v);
        });

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);

        mAuth = FirebaseAuth.getInstance();
    }
    public void gotoRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
    }

    public void login(View view) {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (email.isEmpty() || password.isEmpty()){
            Toast.makeText(LoginActivity.this, "Please add your email and password!", Toast.LENGTH_LONG).show();
        } else{
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Successful login" , Toast.LENGTH_LONG).show();
                    gotoCrypto();
                } else {
                    Toast.makeText(LoginActivity.this, "Failed to login!\n" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void gotoCrypto(){
        Intent intent = new Intent(this, CryptoActivity.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
        emailEditText.setText("");
        passwordEditText.setText("");
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", emailEditText.getText().toString());
        editor.putString("password", passwordEditText.getText().toString());
        editor.apply();
    }
}