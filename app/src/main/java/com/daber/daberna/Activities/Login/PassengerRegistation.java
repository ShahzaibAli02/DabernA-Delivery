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
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daber.daberna.Activities.Passenger.Destination_Selector;
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

public class PassengerRegistation extends AppCompatActivity implements View.OnClickListener {



    EditText  editTextUserName,editTextUserEmail,editTextUserPassword,editTextPhone;
    Button btnSignUp;
    CheckBox checkBoxPolicy;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_registation);
        init();

    }

    private void init()
    {

        editTextUserName=findViewById(R.id.edttxtName);
        editTextUserEmail=findViewById(R.id.edttxtEmail);
        editTextUserPassword=findViewById(R.id.edttxtPassword);
        editTextPhone=findViewById(R.id.edttxtphone);

        checkBoxPolicy=findViewById(R.id.chkbxPolicy);
       // BUTTON SIGN UP
        btnSignUp=findViewById(R.id.btnSignUp);



        editTextUserName.setOnClickListener(this);
        editTextUserEmail.setOnClickListener(this);
        editTextUserPassword.setOnClickListener(this);
        editTextPhone.setOnClickListener(this);

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


            if(doValidation() )
            {

                checkForUserExist();


            }

        }


    }

    private void checkForUserExist()
    {


        final ProgressDialog progressDialog = Dialogs.showLoadingDialog(PassengerRegistation.this, "Just A Second...");
        FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.Db_Accounts)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(!isActivityRunning)
                        return;
                    editTextPhone.setError(null);
                    editTextUserEmail.setError(null);
                    for(DataSnapshot snapshot:dataSnapshot.getChildren())
                    {

                        DriverRegistrationModel value = snapshot.getValue(DriverRegistrationModel.class);


                        if(value.getPhone().equalsIgnoreCase(editTextPhone.getText().toString()))
                        {
                            progressDialog.dismiss();
                            editTextPhone.setError("This Number Is Already  In Use");
                            editTextPhone.requestFocus();
                            return;
                        }
                        if(value.getEmail().equalsIgnoreCase(editTextUserEmail.getText().toString()))
                        {
                            progressDialog.dismiss();
                            editTextUserEmail.setError("This Email Is Already  In Use");
                            editTextUserEmail.requestFocus();
                            return;
                        }

                    }
                    progressDialog.dismiss();
                    uploadToDb();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

    }

    private boolean doValidation()
    {

        for(EditText edttxtToValidate:new EditText[]{editTextPhone,editTextUserEmail,editTextUserName,editTextUserPassword})
        {

            if(TextUtils.isEmpty(edttxtToValidate.getText()))
            {
                edttxtToValidate.setError("This Field Is Required");
                edttxtToValidate.requestFocus();
                return false;
            }
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(editTextUserEmail.getText().toString()).matches())
        {


            editTextUserEmail.setError("Invalid Email");
            editTextUserEmail.requestFocus();
            return false;
        }


        return true;
    }

    private void uploadToDb()
    {


        final PassengerRegistrationModel passengerRegistrationModel=new PassengerRegistrationModel();
        passengerRegistrationModel.setEmail(editTextUserEmail.getText().toString());

        passengerRegistrationModel.setPassengerName(editTextUserName.getText().toString());

        passengerRegistrationModel.setPassword(editTextUserPassword.getText().toString());

        passengerRegistrationModel.setPhone(editTextPhone.getText().toString());

        passengerRegistrationModel.setDriver(false);

        final ProgressDialog progressDialog = Dialogs.showLoadingDialog(PassengerRegistation.this, getResources().getString(R.string.Creating_Account));
        DatabaseReference push = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.Db_Accounts)).push();
        passengerRegistrationModel.setPassengerID(push.getKey());
        push.setValue(passengerRegistrationModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {


                if(!isActivityRunning)
                    return;

                progressDialog.dismiss();
                if(task.isSuccessful())
                {

                    Toast.makeText(PassengerRegistation.this,getResources().getString( R.string.account_success),Toast.LENGTH_LONG).show();
                    Constants.Passenger=passengerRegistrationModel;
                    finish();
                    startActivity(new Intent(PassengerRegistation.this, Destination_Selector.class));
                }
                else
                {
                    Dialogs.showDialog(PassengerRegistation.this,getResources().getString( R.string.title_fail),getResources().getString( R.string.account_fail),-1);
                }

            }
        });


    }


    //  VARIABLE TO CHECK IF ACTIVITY RUNNING OR NOT
    boolean isActivityRunning=true;
    @Override
    protected void onDestroy(){
        isActivityRunning=false;
        super.onDestroy();
    }

    //
    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this,Login.class));
        super.onBackPressed();
    }
}
