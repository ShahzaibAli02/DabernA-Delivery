package com.daber.daberna.Activities.Passenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.daber.daberna.Dialogs;
import com.daber.daberna.Model.DriverRegistrationModel;
import com.daber.daberna.Model.PassengerRequest;
import com.daber.daberna.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RequestingDriver extends AppCompatActivity implements View.OnClickListener {


    Boolean isCompleted=false;
    TextView txtDriverName,txtCarModule,txtApprxPrice;
    Button btnAccept,btnDismiss;
    String ID;
    DriverRegistrationModel DriverDetails;
    boolean isActivityRunning=true;
      ProgressDialog progressDialog;
      String Glob_Price;  //Global Varibale Price
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requesting_driver);
        init();  //Initializing Views

        startRequesting();  //Start Requesting For Driver
        startTimer();   //Start Timer Of One Minute
    }

    private void startTimer()
    {


        new CountDownTimer(60000, 1000)  //This Time Run For One Minute
        {
            @Override
            public void onTick(long millisUntilFinished)
            {

                if(isActivityRunning &&  !isCompleted)  //On Every Second Check If Driver Request Is Completed
                {

                    if(progressDialog.isShowing())   //if Driver Request Is Not Completed Then Update Time On Loading Dialog
                    {

                        progressDialog.setMessage("Searching For Driver : "+millisUntilFinished/1000);
                    }

                }
            }

            @Override
            public void onFinish()  //On Completion Of One Minute this Method Will RUn
            {

                if(isActivityRunning &&  !isCompleted)  //Check If Driver Request Is Not Completed
                {

                    if(progressDialog.isShowing())  //If Loading Dialog is Showing Close it
                        progressDialog.dismiss();


                    //Show Not Found Message
                    Dialogs.showDialog(RequestingDriver.this,"Not Found","No Driver Found",-1);  //Code = -1 Means Go Back When User Press Ok

                }

            }
        }.start();

    }

    private void init()
    {
/*
Intialize Views
 */
        txtDriverName=findViewById(R.id.txtDriverName);
        txtApprxPrice=findViewById(R.id.txtPrice);
        txtCarModule=findViewById(R.id.txtCarModule);

        // BUTTON3

        btnAccept=findViewById(R.id.btnAccept);
        btnDismiss=findViewById(R.id.btnDismiss);


        /*
        Set Click Listener
         */
        btnAccept.setOnClickListener(this);
        btnDismiss.setOnClickListener(this);


        /*

            Id Of Passenger Request which is currently Passenger Waiting For Driver
         */
        ID = getIntent().getExtras().getString("ID");


    }


    public  void startRequesting()
    {

        /*
                Method To Search For Driver
         */
        progressDialog = Dialogs.showLoadingDialog(this, "Searching For Driver "); //Start Loading Dialog

        FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.DB_Requests)).child(ID).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) //This Method Will Be Called When there is Change in the Data of Passenger Request
            {

                if(!isActivityRunning)  //If Activity is closed return
                {
                    return;
                }

                PassengerRequest value = dataSnapshot.getValue(PassengerRequest.class); //Get Value And Put in PassengerRequest Model Class

                if (value != null && !value.getAssignedDriver().equalsIgnoreCase("NONE")) //If AssignedDriver Value is Not None
                {
                    isCompleted=true;
                    FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.DB_Requests)).child(ID).child("isCompleted").setValue("True");  //Set Complete On Database
                    String assignedDriverID = value.getAssignedDriver();  //Get Assigned Driver ID
                    progressDialog.setMessage("Driver Found : Fetching Their Information..");  //Update Message On Loading Dialog
                    Glob_Price=value.getPrice();
                    fetchAndDisplayDriverInfo(assignedDriverID,progressDialog); //Now With Assigned Driver ID Get Other Details Of Driver From Database
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fetchAndDisplayDriverInfo(String assignedDriverID, final ProgressDialog progressDialog)
    {
        /*

        This Method is Used To Get Driver Information With Their ID

         */
        FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.Db_Accounts)).child(assignedDriverID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                if(!isActivityRunning)
                    return;
                progressDialog.dismiss();
                if(dataSnapshot.exists())
                {

                    DriverDetails = dataSnapshot.getValue(DriverRegistrationModel.class);
                    setValsOnTxt(Glob_Price);

                }
                else
                {
                    Dialogs.showDialog(RequestingDriver.this,"Error","Failed To Get Driver Info",-1);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                progressDialog.dismiss();
                Dialogs.showDialog(RequestingDriver.this,"Error",databaseError.getMessage(),-1);
            }
        });
    }

    private void setValsOnTxt(String price)
    {

        /*
        Set Values On TXT FIELD
         */


            txtDriverName.setText(String.format("Driver Name          : %s", DriverDetails.getDriverName()));
            txtCarModule.setText(String.format("Car Module           :  %s", DriverDetails.getDriverCarType()));
            txtApprxPrice.setText(String.format("Approximation Price  :  %s", price));

            btnAccept.setEnabled(true);
            btnAccept.setAlpha(1);

    }

    @Override
    protected void onDestroy()
    {
        /*
        When Activity Is Destroy Set request Complete on Database
         */
        isActivityRunning=false;
        FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.DB_Requests)).child(ID).child("isCompleted").setValue("true");
        super.onDestroy();
    }



    @Override
    public void onClick(View v)
    {
        /*

         Click Listener For All Views
         */

        if(v==btnAccept)
        {

            /*

                    If Accept Button is Pressed Go To DriverOnWay.class
             */

            FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.DB_Requests)).child(ID).child("isAccepted").setValue("true"); //Set IsAccepted On Database To True So That the Driver Can know his Request is Accepted
            finish();
            Intent N=new Intent(RequestingDriver.this,DriverOnWay.class);

            /*

                Pass important information  to next activity

             */
            N.putExtra("ID",DriverDetails.getDriverID());
            N.putExtra("DriverName",DriverDetails.getDriverName());
            N.putExtra("CarModule",DriverDetails.getDriverCarType());
            N.putExtra("PlateNo",DriverDetails.getDriverCarPlateNo());
            N.putExtra("Phone",DriverDetails.getPhone());
            N.putExtra("Price",Glob_Price);
            startActivity(N);

            return;
        }
        if(v==btnDismiss)
        {
           onBackPressed();
        }

    }
    @Override
    public void onBackPressed()
    {

        /*

            If User Press Back Which Means He is Not Accepting Driver Request
         */
        FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.DB_Requests)).child(ID).child("isAccepted").setValue("false");
        finish();
        super.onBackPressed();
    }
}
