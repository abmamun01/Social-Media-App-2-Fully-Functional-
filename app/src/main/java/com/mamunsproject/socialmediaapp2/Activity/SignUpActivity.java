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

public class SignUpActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText etMail,etPassword,etConfirmPassword;
    Button loginButton,signUpButton;
    CheckBox checkBox;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        etMail=findViewById(R.id.signUpEmail);
        etPassword=findViewById(R.id.signUpPassword);
        etConfirmPassword=findViewById(R.id.signUpConfirmPassword);
        loginButton=findViewById(R.id.button_login_signup );
        signUpButton=findViewById(R.id.button_Signup );
        checkBox=findViewById(R.id.login_checkBox);
        progressBar=findViewById(R.id.progress_barSignUp);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    etConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    etConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });



        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etMail.getText().toString();
                String password = etPassword.getText().toString();
                String confirm_password =etConfirmPassword.getText().toString();

                if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password) || !TextUtils.isEmpty(confirm_password)) {
                    if (password.equals(confirm_password)) {
                        progressBar.setVisibility(View.VISIBLE);
                        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {


                                if (task.isSuccessful()) {

                                    sendToMain();
                                } else {

                                    String error = task.getException().getMessage();
                                    Toast.makeText(SignUpActivity.this, "Error" + error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    } else {
                        Toast.makeText(SignUpActivity.this, "Password & Confirm are not Matching!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignUpActivity.this, "Please Fill All The Field", Toast.LENGTH_SHORT).show();
                }

            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, Login_Activity.class));
                finish();
            }
        });

    }

    private void sendToMain() {
        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            sendToMain();
        }

    }
}