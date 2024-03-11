package com.example.personalvocab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class CreateAccount extends AppCompatActivity {

    EditText emailtext,passtext,confpasstext;
    Button createacc;
    ProgressBar progressBar;
    TextView logintext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        emailtext = findViewById(R.id.email_text);
        passtext = findViewById(R.id.password_text);
        confpasstext = findViewById(R.id.confirm_password_text);
        createacc = findViewById(R.id.create_account_btn);
        progressBar = findViewById(R.id.progress_bar);
        logintext = findViewById(R.id.login_text_btn);

        createacc.setOnClickListener(v->createAccoun());
        logintext.setOnClickListener(v->finish());

    }
    void createAccoun(){
String email=emailtext.getText().toString();
String password = passtext.getText().toString();
String confirmpass = confpasstext.getText().toString();
boolean isvalidate = validateData(email,password,confirmpass);

if (!isvalidate){
    return;
}
createAccountinFirebase(email,password);
    }

    void createAccountinFirebase(String email,String password){
changeinprogress(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
if (task.isSuccessful()){

    Toast.makeText(CreateAccount.this, "Successfully create account,check your email", Toast.LENGTH_SHORT).show();
    firebaseAuth.getCurrentUser().sendEmailVerification();
    firebaseAuth.signOut();
finish();
}
else {
    Toast.makeText(CreateAccount.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
}
                
            }
        });


    }

    void changeinprogress(boolean inProgress){
        if (inProgress){
            progressBar.setVisibility(View.VISIBLE);
            createacc.setVisibility(View.GONE);
        }
        else {
            progressBar.setVisibility(View.GONE);
            createacc.setVisibility(View.VISIBLE);
        }
    }
    boolean validateData(String email,String password , String confirmpass){
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailtext.setError("Email is invalid");
            return false;
        }
        if (password.length()<6){
            passtext.setError("Password length is invalid");
            return false;
        }
        if (!password.equals(confirmpass)){
            confpasstext.setError("Password not matched");
            return false;
        }
        return true;
    }

}