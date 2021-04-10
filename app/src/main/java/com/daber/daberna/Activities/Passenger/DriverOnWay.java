package com.daber.daberna.Activities.Passenger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daber.daberna.Dialogs;
import com.daber.daberna.R;

public class DriverOnWay extends AppCompatActivity implements View.OnClickListener {







    /*


        This Class Has Approx Same Functionality Like in GotoDestination.class
        if You Dont Understand anything here open this class GotoDestination.class

        Hope Soo You Will Get Your Point there

     */

    String ID, DriverName, CarModule, PlateNo, Phone,Price;
    TextView txtDriverName, txtCarModule, txtPlateNo,txtPrice;

    Button btnCall, btnMsg, btnDone;

    RatingBar ratingDriver, ratingService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_on_way);
        intit();
        getExtras();
    }

    private void intit() {
        txtDriverName = findViewById(R.id.txtDriverName);
        txtPlateNo = findViewById(R.id.txtCarPlateNo);
        txtCarModule = findViewById(R.id.txtCarModule);
        txtPrice=findViewById(R.id.txtPrice);

        //BUTTON
        btnCall = findViewById(R.id.btnCallDriver);
        btnMsg = findViewById(R.id.btnMessageDriver);
        btnDone = findViewById(R.id.btnDone);


        //RATING BAR

        ratingDriver = findViewById(R.id.ratingDriver);
        ratingService = findViewById(R.id.ratingService);


        //CLICK LISTENERS

        btnCall.setOnClickListener(this);
        btnMsg.setOnClickListener(this);
        btnDone.setOnClickListener(this);


    }

    private void getExtras() {

        ID = getIntent().getExtras().getString("ID");
        DriverName = getIntent().getExtras().getString("DriverName");
        CarModule = getIntent().getExtras().getString("CarModule");
        PlateNo = getIntent().getExtras().getString("PlateNo");
        Phone = getIntent().getExtras().getString("Phone");
        Price = getIntent().getExtras().getString("Price");



        setVals();

    }

    private void setVals()
    {

        txtDriverName.setText(String.format("Driver Name          :  %s", DriverName));
        txtCarModule.setText(String.format( "Car Module           :  %s", CarModule));
        txtPlateNo.setText(String.format(   "Plate No             :  %s", PlateNo));
        txtPrice.setText(String.format(   "Price                :  %s", Price));

    }
    @Override
    public void onClick(View v) {


        if (v == btnCall)
        {
            doCall();
            return;
        }
        if(v==btnMsg)
        {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", Phone, null)));
            return;
        }
        if(v==btnDone)
        {
            Dialogs.showConfirmation(this);
        }


    }

    private void doCall()
    {

        if (Build.VERSION.SDK_INT < 23)
        {
            Call();
        }
        else
            {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
            {

                Call();
            }
            else
                {
                final String[] PERMISSIONS_STORAGE = {Manifest.permission.CALL_PHONE};
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 9);
                }
        }
    }

    private void Call()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
        {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+Phone));
            startActivity(callIntent);
        }
        else
            {
            Toast.makeText(this, "Don't Have Permission To Call", Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch(requestCode)
        {
            case 9:
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED)
                {
                    Call();
                }
                else
                {
                    Toast.makeText(this, "Permission Denied..", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }
}
