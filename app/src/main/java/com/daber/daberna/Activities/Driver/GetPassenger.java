package com.daber.daberna.Activities.Driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.location.Location;
import android.os.Bundle;
import android.widget.ListView;

import com.daber.daberna.Activities.Passenger.RequestingDriver;
import com.daber.daberna.Adapter.PassengerAdapter;
import com.daber.daberna.Constants;
import com.daber.daberna.Dialogs;
import com.daber.daberna.Model.DriverRegistrationModel;
import com.daber.daberna.Model.PassengerRequest;
import com.daber.daberna.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.SQLOutput;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class GetPassenger extends AppCompatActivity {


    ListView list;
    ArrayList<PassengerRequest> Arr_List=new ArrayList<>();  // This Array List Is Used To Store All Passengers Data Which are waiting for ride

    boolean isActivtyRunning=true;   //This boolean variable will tell us This activity is running or not

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_passenger);

        init();  //Calling method to initialize the buttons etc

        getPassengers();  // load waiting passengers from database

    }

    private void getPassengers()
    {

        final ProgressDialog progressDialog = Dialogs.showLoadingDialog(this, "Searching For Passengers"); //showing loading bar
        FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.DB_Requests)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) //This method will be called when load data from DB is DOne
            {
                if(isActivtyRunning && progressDialog.isShowing())  //We are checking is this activity is still running or gets closed
                                                                    // if its still running and progress dialog is showing then close it
                {

                    progressDialog.dismiss();

                }

                Arr_List.clear();   //clear Array list if it has some data (Normally its already empty)

                for(DataSnapshot singleValue:dataSnapshot.getChildren())   //Loop On All Passengers Data
                {

                    try
                    {
                        PassengerRequest  request = singleValue.getValue(PassengerRequest.class);  //Get Data From Database And Put it In Model Class Accoding To Data

                        if(isValidated(request))  //Calling method to validate data eg Distance is 2km etc
                        {
                            Arr_List.add(request);
                        }


                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
                if(!isActivtyRunning)
                    return;
                if(Arr_List.size()<1) //if There is No Data in the list show empty dialog
                {

                    Dialogs.showDialog(GetPassenger.this,"Empty","No Passenger Found",-1); //Code =-1 means Go Back When Press Ok
                    list.setAdapter(null);

                }
                else  //Other Wise Set Data On List
                {
                    PassengerAdapter Adapter=new PassengerAdapter(GetPassenger.this,Arr_List);
                    list.setAdapter(Adapter);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)    // This Method Is Called If Database Denies To Provide Data Or there is Error in Database
            {


                if(!isActivtyRunning)
                    return;
                if(progressDialog.isShowing())
                {
                    progressDialog.dismiss();
                }
                Dialogs.showDialog(GetPassenger.this,"Error",databaseError.getMessage(),-1);
            }
        });

    }

    private boolean isValidated(PassengerRequest request)  //Method To Validate User Request
    {


        if(request.getIsCompleted().equalsIgnoreCase("true"))  //Checking if this is already completed
            return false;

        try
        {



            Double PassengertStartLat = Double.parseDouble(request.getStartLat());
            Double PassengerStartLong =Double.parseDouble( request.getStartLong());



            Double PassengerEndLat = Double.parseDouble(request.getEndLat());
            Double PassengerEndLong =Double.parseDouble( request.getEndLong());

            Double myStartlat=Constants.Lat;
            Double myStartlong=Constants.Long;


            Double myEndlat=Constants.WhereToGoLat;
            Double myEndlong=Constants.WhereToGoLong;






            /*
              Calling getTotalDistanceinKm Method To Calculate Total Distance Between Driver Start And User Start Location
             if Distance > 2 its means invalid request
             */
            if(getTotalDistanceinKm(myStartlat,myStartlong,PassengertStartLat,PassengerStartLong)>2)

            {
                  return false;
            }



             /*
              Calling getTotalDistanceinKm Method To Calculate Total Distance Between Driver End Point And User End Poinr
             if Distance > 2 its means invalid request
             */
            if(getTotalDistanceinKm(myEndlat,myEndlong,PassengerEndLat,PassengerEndLong)>2)
            {
                return false;
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return true;
        }



        if(getTimeDifference(request.getDateTime())>5)  //Calling This Method To Check If This Request is more then 5 minutes late
        {
            return false;
        }

        return true;
    }

    public long getTimeDifference(String RequestDate)
    {
        /*
        This Method Calculates Time Difference Between Two Dates
        */

            try
            {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
                String currentDateandTime = sdf.format(new Date());

                Date date1 = sdf.parse(currentDateandTime);  //This is Current Date

                Date date2 = sdf.parse(RequestDate);  //This Is Request Data

                long diff = date1.getTime()-date2.getTime();  //Time Difference Between These Dates
                long seconds = diff / 1000; //Converting Time Diffence From Milis to Seconds
                long minutes = seconds / 60;  //Converting Seconds To Minutes (Because We Need Time Differnece In Minutes)

                return minutes;

            } catch (ParseException e)
            {

                e.printStackTrace();
                return 0;
            }

    }




    public float getTotalDistanceinKm(Double latA,Double LonA,Double LatB,Double LongB)
    {

        /*
                This Method Calculates Total Distance Between Two Points by Lat And Long
         */

        Location locationA = new Location("Starting Point");
        locationA.setLatitude(latA);
        locationA.setLongitude(LonA);

        Location locationB = new Location("Ending Point");

        locationB.setLatitude(LatB);
        locationB.setLongitude(LongB);

        float distance = locationA.distanceTo(locationB);  //Distance In Meteres

        return distance/1000; // Converting Distance Which Are Currently In Meters to Kilo Meters By Dividing to 1000    ( Km=Meters/1000 )
    }

    @Override
    protected void onDestroy()  //This Method is called when activites get closed
    {
        isActivtyRunning=false;
        super.onDestroy();
    }

    private void init() //Initialize Component
    {
        list=findViewById(R.id.list);
    }
}
