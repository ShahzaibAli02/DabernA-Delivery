package com.daber.daberna;

import com.daber.daberna.Model.DriverRegistrationModel;
import com.daber.daberna.Model.PassengerRegistrationModel;

public class Constants
{
    /*
        This Class Has SomeVarianles Which Are Used In Other Classes
     */

    public static  PassengerRegistrationModel  Passenger;  //Used To Hold Current Logged In Passenger Information
    public  static  DriverRegistrationModel     Driver;  //Used To Hold Current Logged In Driver Information

    public  static  Double Lat=-1.0;   //Used To Store Current Location Lattitude
    public  static  Double Long=-1.0;  //Used To Store Current Location Longnitude

    public  static  Double WhereToGoLat=0.00;  //Used To Store Driver Going  Location Lattitude
    public  static  Double WhereToGoLong=0.00; //Used To Store Driver Going Location Lattitude
    public  static String WhereToGoAddress=null;  //Used To Store Driver Going Location Address in TEXT



}