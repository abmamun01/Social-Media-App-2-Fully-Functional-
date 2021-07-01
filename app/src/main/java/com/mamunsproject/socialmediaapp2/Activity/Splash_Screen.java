package com.mamunsproject.socialmediaapp2.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.mamunsproject.socialmediaapp2.MainActivity;
import com.mamunsproject.socialmediaapp2.R;

public class Splash_Screen extends AppCompatActivity {

    ImageView imageView;
    TextView name1,name2;
    long animTime=2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        imageView=findViewById(R.id.logoSplash);
        name1=findViewById(R.id.splash_name);
        name2=findViewById(R.id.splash_na3me);


        ObjectAnimator animatorY=ObjectAnimator.ofFloat(imageView,"y",400f);
        ObjectAnimator animatorName=ObjectAnimator.ofFloat(name1,"x",200f);
        animatorY.setDuration(animTime);
        animatorName.setDuration(animTime);
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playTogether(animatorY,animatorName);
        animatorSet.start();


        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(Splash_Screen.this, MainActivity.class));
                finish();
            }
        },5000);


    }
}