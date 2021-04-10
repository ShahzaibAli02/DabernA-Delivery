package com.daber.daberna;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

public  class Dialogs
{


    /*
    This Class Some Static Functions
    This Is Class Is Used To Show Dialogs Like Loading Dialog And Message Dialog
     */







    /*

    This Method is used to show Loading bar it Take Two Arguments One Context (From Which Activity We Are Calling This)
                                    And
                    Second is Message Which Message To Show While With Loading Bar Eg Authenticating ,
     */
    public static ProgressDialog showLoadingDialog(Context context, String Message)
    {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(Message);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
        return  progressDialog;
    }



    /*

    This Method is to Show Message Box It Take 4 Arguments 1 is context 2nd is Title To Show On Box 3rd Is Messgae

    and 4th is Code  Here Code Can Be 0 or -1  ... Code 0 Means When user Press Ok Remain On Same Activity
     And Code = -1 Means When User PRess Ok Go To Previous Activity

     */
    public  static  void showDialog(Context context,String Title,String Message,final int code)
    {

        final Activity activity= (Activity) context;
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        View child = activity.getLayoutInflater().inflate(R.layout.dialog_custom, null);  //We Are inflating dialog layout in the calling activity
        TextView title=child.findViewById(R.id.txtView_title);
        TextView message=child.findViewById(R.id.txtView_message);
        Button btn_action=child.findViewById(R.id.btn_ok);
        title.setText(Title);
        message.setText(Message);
        btn_action.setText("Ok");
        builder1.setView(child);
        builder1.setCancelable(false);
        final AlertDialog dialog = builder1.create();
        dialog.show();
        btn_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                if(code==-1) //EXIT CODE
                {
                    activity.onBackPressed();
                }
            }
        });

    }





    /*

    This Method Is To Show Confirmation Message After Driver Press Finish Or Passeneger Press Done Button

     */
    public  static  void showConfirmation(Context context)
    {

        final Activity activity= (Activity) context;
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        View child = activity.getLayoutInflater().inflate(R.layout.dialog_custom, null);
        TextView title=child.findViewById(R.id.txtView_title);
        TextView message=child.findViewById(R.id.txtView_message);
        Button btn_actionOk=child.findViewById(R.id.btn_ok);
        Button btn_actionNo=child.findViewById(R.id.btn_No);
        title.setText("Attention");
        message.setText("Are You Sure To Finish The Trip ?");
        btn_actionOk.setText("Yes");
        btn_actionNo.setVisibility(View.VISIBLE);
        builder1.setView(child);
        builder1.setCancelable(false);
        final AlertDialog dialog = builder1.create();
        dialog.show();
        btn_actionOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();

                    activity.onBackPressed();
            }
        });
        btn_actionNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });

    }

}
