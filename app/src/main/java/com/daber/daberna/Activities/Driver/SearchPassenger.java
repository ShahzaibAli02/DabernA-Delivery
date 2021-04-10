package com.daber.daberna.Activities.Driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.daber.daberna.Activities.EasyLocationProvider;
import com.daber.daberna.Activities.Login.Login;
import com.daber.daberna.Activities.MapsActivity;
import com.daber.daberna.Activities.Passenger.Destination_Selector;
import com.daber.daberna.Constants;
import com.daber.daberna.Dialogs;
import com.daber.daberna.R;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SearchPassenger extends AppCompatActivity implements View.OnClickListener {





    TextView txtwhereToGo;
    Button btnSearchPassenger,btnLogout;
    public static final int REQUEST_LOCATION_CODE = 99;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_passenger);
        init();
    }

    private void init()
    {

        txtwhereToGo=findViewById(R.id.txtwhereToGo);
        btnSearchPassenger=findViewById(R.id.btnSearchPassenger);
        btnLogout=findViewById(R.id.btnlogout);

        btnSearchPassenger.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        txtwhereToGo.setOnClickListener(this);


        if(Constants.WhereToGoAddress!=null)  //WhereToGoAddress in Constants Class Contain Where To Go Address if Thers is Some Address in this variable then show on text field
        {
            txtwhereToGo.setText(Constants.WhereToGoAddress);
        }


        if(Constants.Lat==-1.0)
        {
            /*
            Constants.Lat==-1 Means User Current Location Is Invalid
             So Get Current Location

             */
            getlocation();
        }


    }
    private void getlocation()
    {

        /*
            Check If Application Has Permission To Get Location
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            checkLocationPermission();

        }

        final ProgressDialog progressDialog = Dialogs.showLoadingDialog(SearchPassenger.this, "Loading Current Location"); //Show Loading Dialog

        EasyLocationProvider easyLocationProvider; //Instance  of EasyLocationProvider class

        easyLocationProvider = new EasyLocationProvider.Builder(this)
                .setInterval(5000)
                .setFastestInterval(2000)
                //.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setListener(new EasyLocationProvider.EasyLocationCallback()
                {

                    @Override
                    public void onGoogleAPIClient(GoogleApiClient googleApiClient, String message)
                    {
                        Log.e("LocationProvider", "onGoogleAPIClient: " + message);
                    }



                    /*
                    This Mehtod is Called With Current Lat And Long Of User
                     */
                    @Override
                    public void onLocationUpdated(double latitude, double longitude) {

                        if(progressDialog.isShowing())  //Close Progress Bar
                            progressDialog.dismiss();



                        Constants.Lat=latitude;         //Save  User Current Location Lattitude  in Constants Class Lat Variable
                        Constants.Long=longitude;       //Save  User Current Location Longnitude  in Constants Class Long Variable


                        btnSearchPassenger.setEnabled(true); // Enable Search Button
                        btnSearchPassenger.setAlpha(1);        // Increase Visibliy Of Button


                    }

                    @Override
                    public void onLocationUpdateRemoved()
                    {
                        Log.e("LocationProvider","onLocationUpdateRemoved");
                    }
                }).build();

        getLifecycle().addObserver(easyLocationProvider);


    }



    /*

        This Method Will Be Called When User Allows Or Denys Permission Request
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        finish();
                        startActivity(getIntent());
                    }
                } else {
                    finish();
                    Toast.makeText(this, "Permission Denied Exiting", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }



    /*
             Method To Check Location Permission
     */
    public boolean checkLocationPermission()
    {



        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {

            /*
                IF Permission Is Not Granted Request For Permission
             */
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed()
    {
        ActivityCompat.finishAffinity(this);  //Close All Open Activities
        Constants.Driver=null;                       // Erase Current driver Details
        startActivity(new Intent(SearchPassenger.this, Login.class));  //Start Login Activity
        super.onBackPressed();
    }

    @Override
    public void onClick(View v)
    {


        if(v==btnLogout)
        {
            onBackPressed();
            return;
        }
        if(v==txtwhereToGo)  //If Where To Go Button Is Pressed Open Maps Activity And PutExtrs WhereTOGo So that MapsActivity can know from where we call it
        {
            finish();
            startActivity(new Intent(SearchPassenger.this, MapsActivity.class).putExtra("Dest","WhereTOGo"));

        }
        if(v==btnSearchPassenger)
        {



            if(Constants.Lat==-1.0)
            {
                Toast.makeText(SearchPassenger.this,"Current Location Error | Please Restart Application",Toast.LENGTH_LONG).show();
                return;
            }
            if(Constants.WhereToGoAddress!=null)
            {

                startActivity(new Intent(SearchPassenger.this,GetPassenger.class));
            }
            else
            {
                Toast.makeText(SearchPassenger.this,"Please Select Where To Go",Toast.LENGTH_LONG).show();
            }


        }
    }
}
