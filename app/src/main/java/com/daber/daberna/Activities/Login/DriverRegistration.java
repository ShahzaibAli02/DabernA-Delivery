package com.daber.daberna.Activities.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.TextUtilsCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daber.daberna.Activities.Driver.SearchPassenger;
import com.daber.daberna.Constants;
import com.daber.daberna.Dialogs;
import com.daber.daberna.Model.DriverRegistrationModel;
import com.daber.daberna.Model.PassengerRegistrationModel;
import com.daber.daberna.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DriverRegistration extends AppCompatActivity implements View.OnClickListener {



    EditText  editTextUserName,editTextUserEmail,editTextUserPassword,editTextPhone,editTextCarType,editTextPlateNo;
    Button btnSignUp;
    CheckBox checkBoxPolicy;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_registration);
        init();

    }

    private void init()
    {

        editTextUserName=findViewById(R.id.edttxtName);
        editTextUserEmail=findViewById(R.id.edttxtEmail);
        editTextUserPassword=findViewById(R.id.edttxtPassword);
        editTextPhone=findViewById(R.id.edttxtphone);
        editTextCarType=findViewById(R.id.edttxtcarType);
        editTextPlateNo=findViewById(R.id.edttxtPlateNo);

        checkBoxPolicy=findViewById(R.id.chkbxPolicy);
        // BUTTON SIGN UP
        btnSignUp=findViewById(R.id.btnSignUp);



        editTextUserName.setOnClickListener(this);
        editTextUserEmail.setOnClickListener(this);
        editTextUserPassword.setOnClickListener(this);
        editTextPhone.setOnClickListener(this);
        editTextCarType.setOnClickListener(this);
        editTextPlateNo.setOnClickListener(this);

        checkBoxPolicy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked)
                {
                    btnSignUp.setAlpha(0.5f);
                    btnSignUp.setEnabled(false);
                }
                else
                {
                    btnSignUp.setAlpha(1);
                    btnSignUp.setEnabled(true);
                }
            }
        });
        btnSignUp.setOnClickListener(this);



    }


    @Override
    public void onClick(View v) {


        if (v == btnSignUp)

        {


            if(doValidation() )   //Validate All Data
            {

                checkForUserExist();


            }

        }


    }

    private void checkForUserExist()
    {


        final ProgressDialog progressDialog = Dialogs.showLoadingDialog(DriverRegistration.this, "Just A Second...");
        FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.Db_Accounts)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!isActivityRunning)
                    return;


                editTextPhone.setError(null);  //Remove Error On PHone Field If Any
                editTextUserEmail.setError(null);   //Remove Error On Email Field If Any


                for(DataSnapshot snapshot:dataSnapshot.getChildren())  //Get All Accounts On Database
                {

                    DriverRegistrationModel value = snapshot.getValue(DriverRegistrationModel.class);


                    if(value.getPhone().equalsIgnoreCase(editTextPhone.getText().toString()))  // If Phone Number Matches set Phone Number Error
                    {
                        progressDialog.dismiss();
                        editTextPhone.setError("This Number Is Already  In Use");
                        editTextPhone.requestFocus();
                        return;
                    }
                    if(value.getEmail().equalsIgnoreCase(editTextUserEmail.getText().toString()))  //If Email Matches Set Email Error
                    {
                        progressDialog.dismiss();
                        editTextUserEmail.setError("This Email Is Already  In Use");
                        editTextUserEmail.requestFocus();
                        return;
                    }

                }



                // If User Doesnt Exist upload Data To Database
                uploadToDb();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private boolean doValidation()
    {


        /*
            Loop On All EditText Fields To Check If Any Is Empty
         */
        for(EditText edttxtToValidate:new EditText[]{editTextPhone,editTextUserEmail,editTextUserName,editTextUserPassword,editTextPlateNo,editTextCarType})
        {

            if(TextUtils.isEmpty(edttxtToValidate.getText()))
            {
                edttxtToValidate.setError("This Field Is Required");
                edttxtToValidate.requestFocus();
                return false;
            }
        }


         /*
                      Email Field Validation
          */
     if(!Patterns.EMAIL_ADDRESS.matcher(editTextUserEmail.getText().toString()).matches())
        {

            editTextUserEmail.setError("Invalid Email");
            editTextUserEmail.requestFocus();
            return false;
        }




        return true;
    }

    private void uploadToDb()  //Upload Data To Database
    {


        final ProgressDialog progressDialog = Dialogs.showLoadingDialog(DriverRegistration.this, "Creating Your Account..");

        DatabaseReference push = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.Db_Accounts)).push(); //DATABASE REFERENCE

        final DriverRegistrationModel driverRegistrationModel=new DriverRegistrationModel();  //Object Of Model Class Of Driver


        /*

         Getting Values From EditText Fields And Putting In ModelClass Object

         */
        driverRegistrationModel.setEmail(editTextUserEmail.getText().toString());

        driverRegistrationModel.setDriverName(editTextUserName.getText().toString());

        driverRegistrationModel.setPassword(editTextUserPassword.getText().toString());

        driverRegistrationModel.setPhone(editTextPhone.getText().toString());

        driverRegistrationModel.setDriverCarPlateNo(editTextPlateNo.getText().toString());

        driverRegistrationModel.setDriverCarType(editTextCarType.getText().toString());

        driverRegistrationModel.setDriver(true);
        driverRegistrationModel.setDriverID(push.getKey());



        push.setValue(driverRegistrationModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {


                if(!isActivityRunning)
                    return;

                progressDialog.dismiss();

                if(task.isSuccessful())  //If Task Is Successfull Go To Driver Main Screen
                {

                    Toast.makeText(DriverRegistration.this,getResources().getString( R.string.account_success),Toast.LENGTH_LONG).show();
                    Constants.Driver=driverRegistrationModel;
                    finish();
                    startActivity(new Intent(DriverRegistration.this, SearchPassenger.class));
                }
                else  //Else show Error
                {
                    Dialogs.showDialog(DriverRegistration.this,getResources().getString( R.string.title_fail),getResources().getString( R.string.account_fail),-1);
                }

            }
        });


    }
    //  VARIABLE TO CHECK IF ACTIVITY RUNNING OR NOT
    boolean isActivityRunning=true;
    @Override
    protected void onDestroy()
    {
        isActivityRunning=false;
        super.onDestroy();
    }



    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this,Login.class));
        super.onBackPressed();
    }
}
