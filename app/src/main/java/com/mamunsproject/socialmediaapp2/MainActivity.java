package com.mamunsproject.socialmediaapp2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mamunsproject.socialmediaapp2.Activity.Login_Activity;
import com.mamunsproject.socialmediaapp2.Fragment.Ask_Fragment;
import com.mamunsproject.socialmediaapp2.Fragment.Home_Fragment;
import com.mamunsproject.socialmediaapp2.Fragment.Profile_Fragment;
import com.mamunsproject.socialmediaapp2.Fragment.Queue_Fragment;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if (user==null){
            startActivity(new Intent(MainActivity.this,Login_Activity.class));
            finish();
        }


        auth = FirebaseAuth.getInstance();

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new Home_Fragment()).commit();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @org.jetbrains.annotations.NotNull MenuItem item) {

                Fragment selected = null;
                switch (item.getItemId()) {

                    case R.id.profile_bottom:
                        selected = new Profile_Fragment();
                        break;

                    case R.id.ask_bottom:
                        selected = new Ask_Fragment();
                        break;

                    case R.id.queue_bottom:
                        selected = new Queue_Fragment();
                        break;

                    case R.id.home_bottom:
                        selected = new Home_Fragment();
                        break;

                }

                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,
                        selected).commit();
                return true;
            }
        });
    }


    public void logOut(View view) {
        auth.signOut();
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            startActivity(new Intent(getApplicationContext(), Login_Activity.class));
            finish();
        }
    }
}