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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);



        //finish();
      //  startActivity(new Intent(SplashScreen.this, Login.class));

        System.out.println( FirebaseStorage.getInstance().getReference().getBucket());
        FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if(task.isSuccessful())
                {
                    System.out.println("SUCCES");
                }
                else
                {
                    System.out.println("ERROR : "+task.getException().getMessage());
                }


            }
        });
       /* new CountDownTimer(3000, 1000) //Start Timer For 3 Seconds
        {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish()  // After 3 Seconds Complete Go to Next Activity LoginClass
            {

            }
        }.start();

        */


    }

}
