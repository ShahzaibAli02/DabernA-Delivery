package com.daber.daberna.Activities.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.daber.daberna.Activities.Driver.SearchPassenger;
import com.daber.daberna.Activities.Passenger.Destination_Selector;
import com.daber.daberna.Constants;
import com.daber.daberna.Dialogs;
import com.daber.daberna.Model.DriverRegistrationModel;
import com.daber.daberna.Model.PassengerRegistrationModel;
import com.daber.daberna.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity implements View.OnClickListener

{


    Button btnDrivReg,btnPasReg,btnlogin;
    EditText editTextUserPassword,editTextUserEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init()  //Initilazing Views
    {

        btnDrivReg=findViewById(R.id.btnDriverReg);
        btnPasReg=findViewById(R.id.btnPassReg);
        btnlogin=findViewById(R.id.btnLogin);
        editTextUserPassword=findViewById(R.id.edttxtPassword);
        editTextUserEmail=findViewById(R.id.edttxtEmail);



        /*
              Set Click Listeners Of Views
         */
        btnlogin.setOnClickListener(this);
        btnPasReg.setOnClickListener(this);
        btnDrivReg.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) //Click Listener Of  all Views
    {

        if(v==btnlogin)
        {
            doValidation();   //If Button Login Is Pressed Authenticate the provided Information
        }

        if(v==btnDrivReg) //If Button Driver Registration Is Pressed Go To Driver Reg Class
        {
            finish();
            startActivity(new Intent(Login.this,DriverRegistration.class));
            return;
        }

        if(v==btnPasReg)  //If Button Passenger Registration Is Pressed Go To Passenger Reg Class
        {

            finish();
            startActivity(new Intent(Login.this,PassengerRegistation.class));
            return;
        }


    }

    private void doValidation() // Function to Check If Any Field Is Empty
    {
        for(EditText edttxtToValidate:new EditText[]{editTextUserEmail,editTextUserPassword})
        {

            if(TextUtils.isEmpty(edttxtToValidate.getText()))
            {
                edttxtToValidate.setError("This Field Is Required");
                edttxtToValidate.requestFocus();
                return;
            }
        }

        /*
            if All Fields Are Provided
            Then Authenticate The Provided Information
         */

        Authenticate();

    }

    private void Authenticate()
    {
        final  ProgressDialog progressDialog=Dialogs.showLoadingDialog(this,"Authenticating...");

        FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.Db_Accounts)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                for(DataSnapshot child:dataSnapshot.getChildren())  //Get all Accounts And Check The Provided Details Are There
                {


                    DriverRegistrationModel value = child.getValue(DriverRegistrationModel.class);

                    if(value.getPassword().equals(editTextUserPassword.getText().toString()) && value.getEmail().equalsIgnoreCase(editTextUserEmail.getText().toString()))
                    {

                        progressDialog.dismiss();

                        // Account Found  Now Checking if the account id of Passenger Or Driver getDriver Function Returns True If Account is Of Driver
                        if(value.getDriver())
                        {


                            /*
                                if Account Is Of Driver Then Go To Driver Main Screen
                             */
                            Constants.Driver=value;   //Save Current Driver Details In A Variable In Contants Class
                            finish();
                            startActivity(new Intent(Login.this, SearchPassenger.class));
                            return;
                        }
                        else
                        {

                              /*
                                if Account Is Of Passeneger Then Go To Passeneger Main Screen
                             */
                            Constants.Passenger=child.getValue(PassengerRegistrationModel.class);  //Save Current Passeneger Details In A Variable In Contants Class
                            finish();
                            startActivity(new Intent(Login.this, Destination_Selector.class));
                            return;
                        }

                    }

                }


                if(!isActivityRunning)
                    return;
                else
                {
                    progressDialog.dismiss();
                    Dialogs.showDialog(Login.this,getResources().getString(R.string.authentication_fail),getResources().getString(R.string.email_password_error),0);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        ActivityCompat.finishAffinity(this);
        super.onBackPressed();
    }
    //  VARIABLE TO CHECK IF ACTIVITY RUNNING OR NOT
    boolean isActivityRunning=true;
    @Override
    protected void onDestroy(){
        isActivityRunning=false;
        super.onDestroy();
    }

}
