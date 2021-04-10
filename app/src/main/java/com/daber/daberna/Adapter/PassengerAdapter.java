package com.daber.daberna.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;


import com.daber.daberna.Activities.Driver.GotoDestination;
import com.daber.daberna.Constants;
import com.daber.daberna.Dialogs;
import com.daber.daberna.Model.PassengerRequest;
import com.daber.daberna.R;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class PassengerAdapter extends BaseAdapter {


    /*

    This One Is Passenger List Adapter

     */
    ArrayList<PassengerRequest> data;
    Context context;

    public PassengerAdapter(Context context, ArrayList<PassengerRequest> data)
    {
        this.context = context;
        this.data = data;
    }



    @Override
    public int getCount()  //  Number Of Items In  List To Create
    {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder")
        View row = inflater.inflate(R.layout.lyt_passenger, parent, false);


        final PassengerRequest Data = data.get(position);  //Get Passenger Details At  this Position

        try
        {
            final Activity ac= (Activity) context;

            TextView txtName = row.findViewById(R.id.txtName);
            TextView txtKind = row.findViewById(R.id.txtKind);
            TextView txtNoOfPass = row.findViewById(R.id.txtNoOfPass);
            TextView txtPrice = row.findViewById(R.id.txtPrice);
            TextView txtAddress= row.findViewById(R.id.txtAddress);

            Button btnGet=row.findViewById(R.id.btnget);


            /*

                Setting Current Passenger Details
             */

            txtName.setText(String.format("Name  : %s", Data.getName()));
            txtKind.setText(String.format("Kind : %s", Data.getKind()));
            txtNoOfPass.setText(String.format("No Of Passengers  : %s", Data.getNoOfPassengers()));
            txtPrice.setText(String.format("Price : %s", Data.getPrice()));
            txtAddress.setText(String.format("Address  : %s", Data.getAddress()));


            btnGet.setOnClickListener(new View.OnClickListener()  //if Button Get Is Pressed


            {
                @Override
                public void onClick(View v)
                {


                    try {


                        /*
                            In The Current Passenger Request Put Current Driver Id In The Field assignedDriver
                            So That The User Can Gets The Assigned Driver Data
                         */
                        FirebaseDatabase.getInstance().getReference(ac.getResources().getString(R.string.DB_Requests)).child(Data.getRequestID()).child("assignedDriver").setValue(Constants.Driver.getDriverID());




                        /*
                            Close This Activity And Go To Next Class And Pass Some Values Which Are To Be Required In Next Class
                         */
                        ac.finish();
                        Intent N = new Intent(ac, GotoDestination.class);
                        N.putExtra("ID", Data.getRequestID());
                        N.putExtra("Name", Data.getName());
                        N.putExtra("Price", Data.getPrice());
                        N.putExtra("StartLat", Data.getStartLat());
                        N.putExtra("StartLong", Data.getStartLong());
                        N.putExtra("EndLat", Data.getEndLat());
                        N.putExtra("Endlong", Data.getEndLong());
                        N.putExtra("Phone", Data.getPhone());
                        ac.startActivity(N);
                    }
                    catch (Exception e)
                    {

                        /*
                        If Any Error Occurs Show Error Message
                         */
                        Dialogs.showDialog(context,"ERROR IN BTNCLICK",e.getMessage(),0);
                    }



                }
            });


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        return row;
    }
}