package com.daber.daberna.Activities.Driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daber.daberna.Activities.Passenger.RequestingDriver;
import com.daber.daberna.Constants;
import com.daber.daberna.Dialogs;
import com.daber.daberna.Model.PassengerRequest;
import com.daber.daberna.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GotoDestination extends AppCompatActivity implements View.OnClickListener {


    String ID,Name,Price,StartLat,StartLong,EndLat,Endlong,Phone;
    TextView txtName, txtPrice;

    Button btnCall, btnMsg, btnGotToPass,btnGotoDest,btnFinish;

    boolean isActivityRunning=true;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goto_destination);
      try
      {

          intit();  //Initialize Views

          getExtras(); //Get Values Passed From Previous Activity (User Details)

          startRequesting();  //Start Requesting To User To Accept Our Request

          startTimer();  //Start Timer Of One Minute
      }
      catch (Exception e)
      {
          Dialogs.showDialog(this,"Error In GOTODEST : ",e.getMessage(),0);
      }

    }
    private void startTimer()
    {


        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished)  //This Mehod is Called After Every Second
            {

                if(isActivityRunning)
                {

                    if(progressDialog.isShowing())
                    {

                        progressDialog.setMessage("Waiting For User To Accept : "+millisUntilFinished/1000);
                    }
                    else
                    {
                        this.cancel();
                    }

                }
            }

            @Override
            public void onFinish()  //This Method Will Be Called After One Minute
            {

                if(isActivityRunning)
                {

                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                    Dialogs.showDialog(GotoDestination.this,"Time Out","Passenger Didnt Accepted Your Request in Given Time",-1);

                }

            }
        }.start();

    }
    private void startRequesting()
    {


         progressDialog = Dialogs.showLoadingDialog(GotoDestination.this, "Waiting For User To Accept");
         FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.DB_Requests)).child(ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) //this method will be called when theres is some change in request data on database
            {
                if(!isActivityRunning)
                {
                    return;
                }

                PassengerRequest value = dataSnapshot.getValue(PassengerRequest.class);

                if (value != null && value.getIsAccepted().equalsIgnoreCase("true")) //if Accepted
                {
                    progressDialog.dismiss();
                }
                if (value != null && value.getIsAccepted().equalsIgnoreCase("false")) //if not accepted show rejected message
                {
                    progressDialog.dismiss();
                    Dialogs.showDialog(GotoDestination.this,"Reject","User Didnt Accepted Your Request",-1);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void intit()
    {
        txtName = findViewById(R.id.txtName);
        txtPrice = findViewById(R.id.txtPrice);


        //BUTTON
        btnCall = findViewById(R.id.btnCall);
        btnMsg = findViewById(R.id.btnMessage);
        btnGotToPass = findViewById(R.id.btngotoPassenger);
        btnGotoDest = findViewById(R.id.btnGotoDestination);
        btnFinish=findViewById(R.id.btnFinish);





        //CLICK LISTENERS

        btnCall.setOnClickListener(this);
        btnMsg.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
        btnGotoDest.setOnClickListener(this);
        btnGotToPass.setOnClickListener(this);


    }

    private void getExtras() {

        ID = getIntent().getExtras().getString("ID");
        Name = getIntent().getExtras().getString("Name");
        Price = getIntent().getExtras().getString("Price");
        StartLat = getIntent().getExtras().getString("StartLat");
        StartLong = getIntent().getExtras().getString("StartLong");
        EndLat = getIntent().getExtras().getString("EndLat");
        Endlong = getIntent().getExtras().getString("Endlong");
        Phone = getIntent().getExtras().getString("Phone");


        setVals();

    }

    private void setVals()
    {

        txtName.setText(String.format("Name          :  %s", Name));
        txtPrice.setText(String.format( "Price           :  %s", Price));

    }

    @Override
    protected void onDestroy() {
        isActivityRunning=false;
        super.onDestroy();
    }

    @Override
    public void onClick(View v)
    {


        if (v == btnCall)  //if btn call is clicked
        {
            doCall();
            return;
        }
        if(v==btnMsg) //if button Message is Clicked
        {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", Phone, null)));
            return;
        }
        if(v==btnFinish)   //if button Message is Clicked
        {
            Dialogs.showConfirmation(this);
        }
        if (v==btnGotoDest)    //if button Go To Destination Is Clicked
        {

            btnFinish.setEnabled(true);
            btnFinish.setAlpha(1);
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?saddr="+StartLat+","+StartLong+"&daddr="+EndLat+","+Endlong+""));
            startActivity(intent);
        }
        if(v==btnGotToPass) //if button Go To Passenger Is Clicked
        {

            btnGotoDest.setEnabled(true);
            btnGotoDest.setAlpha(1);
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?saddr="+ Constants.Lat+","+Constants.Long+"&daddr="+StartLat+","+StartLong+""));
            startActivity(intent);
        }


    }

    private void doCall()
    {

        /*
        Method To Initiate Call
         */

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

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
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
