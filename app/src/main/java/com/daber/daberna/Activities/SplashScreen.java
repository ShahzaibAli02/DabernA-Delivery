package com.daber.daberna.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import com.daber.daberna.Activities.Login.Login;
import com.daber.daberna.Constants;
import com.daber.daberna.R;
import com.google.android.gms.common.api.GoogleApiClient;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        new CountDownTimer(3000, 1000) //Start Timer For 3 Seconds
        {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish()  // After 3 Seconds Complete Go to Next Activity LoginClass
            {
                finish();
                startActivity(new Intent(SplashScreen.this, Login.class));
            }
        }.start();


    }

}
