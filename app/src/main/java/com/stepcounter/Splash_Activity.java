package com.stepcounter;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.stepcounter.homescreen.Home_Screen_Activity;

public class Splash_Activity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        context=this;

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(context, Home_Screen_Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }
        }, SPLASH_TIME_OUT);

    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}
