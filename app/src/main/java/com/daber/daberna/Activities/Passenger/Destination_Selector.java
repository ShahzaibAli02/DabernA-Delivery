package com.daber.daberna.Activities.Passenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.daber.daberna.Activities.EasyLocationProvider;
import com.daber.daberna.Activities.Login.Login;
import com.daber.daberna.Constants;
import com.daber.daberna.Dialogs;
import com.daber.daberna.Model.PassengerRequest;
import com.daber.daberna.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;

public class Destination_Selector extends AppCompatActivity implements View.OnClickListener
{


   public  static  String EndingPointName;

   public  static  double StartPointLat,StartingPointLng=-1;

    public  static  double EndPointLat,EndPointLng=-1;

    TextView editTextEndingPoint;
    Spinner edttxtNoofPass;

    Button btnGo,btnlogout;
    LinearLayout linear_NoOfPass;
    public static final int REQUEST_LOCATION_CODE = 99;


    CheckBox chkbxRequestDriver,chkbxRequestCargo;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination__selector);

        init();
    }

    private void init()
    {

        editTextEndingPoint=findViewById(R.id.edttxtEndingPoint);
        chkbxRequestDriver=findViewById(R.id.chkbxRequestDriver);
        chkbxRequestCargo=findViewById(R.id.chkbxRequestCargo);
        edttxtNoofPass=findViewById(R.id.edttxtNoofPass);
        btnlogout=findViewById(R.id.btnlogout);
        btnlogout.setOnClickListener(this);
        btnGo=findViewById(R.id.btnGo);
        linear_NoOfPass=findViewById(R.id.linear_NoOfPass);


        edttxtNoofPass.setAdapter(new ArrayAdapter<>(Destination_Selector.this,R.layout.support_simple_spinner_dropdown_item,new String[]{"1","2","3","4","5","6"}));
        chkbxRequestDriver.setOnClickListener(this);
        chkbxRequestCargo.setOnClickListener(this);
        editTextEndingPoint.setOnClickListener(this);

        btnGo.setOnClickListener(this);
        if(EndingPointName!=null)
        {
            editTextEndingPoint.setText(EndingPointName);
        }


       if(StartingPointLng==-1)
                getlocation();

    }


    private void getlocation()
    {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();

        }

        final ProgressDialog progressDialog = Dialogs.showLoadingDialog(Destination_Selector.this, "Loading Current Location");
        EasyLocationProvider easyLocationProvider; //Declare Global Variable
        easyLocationProvider = new EasyLocationProvider.Builder(this)
                .setInterval(5000)
                .setFastestInterval(2000)
                //.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setListener(new EasyLocationProvider.EasyLocationCallback() {
                    @Override
                    public void onGoogleAPIClient(GoogleApiClient googleApiClient, String message) {
                        Log.e("LocationProvider", "onGoogleAPIClient: " + message);
                    }

                    @Override
                    public void onLocationUpdated(double latitude, double longitude) {

                        if(progressDialog.isShowing())
                          progressDialog.dismiss();
                        StartPointLat=latitude;
                        StartingPointLng=longitude;

                        Log.e("LocationProvider","onLocationUpdated:: "+ "Latitude: "+latitude+" Longitude: "+longitude);
                    }

                    @Override
                    public void onLocationUpdateRemoved()
                    {
                        Log.e("LocationProvider","onLocationUpdateRemoved");
                    }
                }).build();

        getLifecycle().addObserver(easyLocationProvider);
    }

    @Override
    public void onClick(View v)
    {



        if(v==editTextEndingPoint)
        {
            finish();
          //  startActivity(new Intent(Destination_Selector.this, MapsActivity.class).putExtra("Dest","end"));

        }
        if(v==chkbxRequestCargo)
        {
            if(chkbxRequestCargo.isChecked())
            {
                linear_NoOfPass.setVisibility(View.GONE);
                chkbxRequestDriver.setChecked(false);
            }
            else
            {
                linear_NoOfPass.setVisibility(View.VISIBLE);
                chkbxRequestDriver.setChecked(true);
            }

        }
        if(v==chkbxRequestDriver)
        {
            if(chkbxRequestDriver.isChecked())
            {
                linear_NoOfPass.setVisibility(View.VISIBLE);
                chkbxRequestCargo.setChecked(false);
            }
            else
            {
                linear_NoOfPass.setVisibility(View.GONE);
                chkbxRequestCargo.setChecked(true);
            }

        }
        if(btnlogout==v)
        {
           onBackPressed();

        }

        if(v==btnGo)
        {


           if(isValidated())
           {
               addinRequests();
           }

        }

    }

    private void addinRequests()
    {

        final ProgressDialog progressDialog = Dialogs.showLoadingDialog(Destination_Selector.this, "Adding Your  Request");
        PassengerRequest Model=new PassengerRequest();
        Model.setAddress(EndingPointName);
        Model.setEmail(Constants.Passenger.getEmail());
        Model.setName(Constants.Passenger.getPassengerName());
        if(chkbxRequestDriver.isChecked())
        {
            Model.setKind("Passeneger");
            Model.setNoOfPassengers(edttxtNoofPass.getSelectedItem().toString());
        }
        else
            Model.setKind("Cargo");

        Model.setStartLat(String.valueOf(StartPointLat));
        Model.setStartLong(String.valueOf(StartingPointLng));
        Model.setEndLat(String.valueOf(EndPointLat));
        Model.setEndLong(String.valueOf(EndPointLng));

        Model.setPhone(String.valueOf(Constants.Passenger.getPhone()));

        Model.setTotalDistance(String.valueOf(getTotalDistanceinKm()));

        DecimalFormat formater = new DecimalFormat("0.00");
        String format = formater.format(2 * getTotalDistanceinKm());
        Model.setPrice(String.format("%s sr", format));


         final DatabaseReference requests = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.DB_Requests)).push();
         Model.setRequestID(requests.getKey());

        requests.setValue(Model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {


                progressDialog.dismiss();
                if(task.isSuccessful())
                {

                    startActivity(new Intent(Destination_Selector.this,RequestingDriver.class).putExtra("ID",String.valueOf(requests.getKey())));
                }
                else
                {
                    Dialogs.showDialog(Destination_Selector.this,"Error",task.getException().getMessage(),0);
                }

            }
        });
    }

    public boolean checkLocationPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);

            }
            return false;
        }
        return true;
    }
    private boolean isValidated() {

        /*
           Validation To check if Starting Location And Ending Location Is Selected Or No
         */
        for (double A : new double[]{StartPointLat, StartingPointLng, EndPointLat, EndPointLng}) {
            if (A == -1) {
                return false;
            }
        }


        /*
        Validation to Check TotalDistance Dont Exceed 2KM

         */

        return true;
    }

    @Override
    public void onBackPressed()
    {
        ActivityCompat.finishAffinity(this);
        Constants.Passenger=null;
        StartingPointLng=-1;
        StartPointLat=-1;
        startActivity(new Intent(Destination_Selector.this, Login.class));
        super.onBackPressed();
    }

    /*
         Method To Get Total Distance Between Two Points In Kms
         */
    public float getTotalDistanceinKm()
    {

        Location locationA = new Location("Starting Point");
        locationA.setLatitude(StartPointLat);
        locationA.setLongitude(StartingPointLng);

        Location locationB = new Location("Ending Point");

        locationB.setLatitude(EndPointLat);
        locationB.setLongitude(EndPointLng);

        float distance = locationA.distanceTo(locationB);  //Distance In Meteres

        return distance/1000; // Converting Distance Which Are Currently In Meters to Kilo Meters By Dividing to 1000    ( Km=Meters/1000 )
    }
}
