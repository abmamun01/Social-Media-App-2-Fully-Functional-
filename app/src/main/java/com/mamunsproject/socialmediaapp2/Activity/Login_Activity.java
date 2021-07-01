package com.mamunsproject.socialmediaapp2.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mamunsproject.socialmediaapp2.MainActivity;
import com.mamunsproject.socialmediaapp2.R;

public class Login_Activity extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText etMail,etPassword;
    Button loginButton,signUpButton;
    CheckBox checkBox;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth=FirebaseAuth.getInstance();

        etMail=findViewById(R.id.loginEmail);
        etPassword=findViewById(R.id.loginPassword);
        loginButton=findViewById(R.id.button_login );
        signUpButton=findViewById(R.id.button_signup );
        checkBox=findViewById(R.id.login_checkBox);
        progressBar=findViewById(R.id.progress_barLogin);


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { 
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });




        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login_Activity.this,SignUpActivity.class));

            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = etMail.getText().toString();
                String password = etPassword.getText().toString();

                if (!TextUtils.isEmpty(email)||!TextUtils.isEmpty(password)){
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                       
                            if (task.isSuccessful()){
                                sendToMain();
                            }else {
                                String error = task.getException().getMessage();
                                Toast.makeText(Login_Activity.this, "Error" + error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else {
                    Toast.makeText(Login_Activity.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void sendToMain() {
        startActivity(new Intent(Login_Activity.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            startActivity(new Intent(Login_Activity.this,MainActivity.class));
            finish();
        }
    }
}