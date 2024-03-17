package com.example.personalvocab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    EditText emailtext, passtext;
    Button login;
    ProgressBar progressBar;
    TextView createacctext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        emailtext = findViewById(R.id.email_text);
        passtext = findViewById(R.id.password_text);
        login = findViewById(R.id.login_btn);
        progressBar = findViewById(R.id.progress_bar);
        createacctext = findViewById(R.id.createacc_text_btn);

        login.setOnClickListener((v) -> loginUsr());
        createacctext.setOnClickListener((v) -> startActivity(new Intent(Login.this, CreateAccount.class)));

    }

    void loginUsr() {
        String email = emailtext.getText().toString();
        String password = passtext.getText().toString();
        boolean isvalidate = validateData(email, password);

        if (!isvalidate) {
            return;
        }
        loginAccountInFirebase(email, password);
    }

    void loginAccountInFirebase(String email, String password) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeinprogress(true);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeinprogress(false);
                if (task.isSuccessful()) {
                    startActivity(new Intent(Login.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(Login.this, "Email not verified,Please verify your email.", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    void changeinprogress(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            login.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            login.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData(String email, String password) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailtext.setError("Email is invalid");
            return false;
        }
        if (password.length() < 6) {
            passtext.setError("Password length is invalid");
            return false;
        }
        return true;
    }
}