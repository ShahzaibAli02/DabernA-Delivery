package com.daber.daberna.Model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PassengerRequest
{


     /*

    Passenger Request Model Class

     */


    private  String Name;
    private  String Email;

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    private  String Phone;




    public String getIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(String isAccepted) {
        this.isAccepted = isAccepted;
    }

    private  String isAccepted;
    public String getRequestID() {
        return RequestID;
    }

    public void setRequestID(String requestID) {
        RequestID = requestID;
    }

    private  String RequestID;

    public String getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(String isCompleted) {
        this.isCompleted = isCompleted;
    }

    private  String isCompleted;


    public String getAssignedDriver() {
        return AssignedDriver;
    }

    public void setAssignedDriver(String assignedDriver) {
        AssignedDriver = assignedDriver;
    }

    private  String AssignedDriver;

    public  PassengerRequest()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        setDateTime(String.valueOf(currentDateandTime));
        setAssignedDriver("NONE");
        setIsCompleted("false");
        setIsAccepted("None");

    }
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public String getKind() {
        return Kind;
    }

    public void setKind(String kind) {
        Kind = kind;
    }

    public String getNoOfPassengers() {
        return NoOfPassengers;
    }

    public void setNoOfPassengers(String noOfPassengers) {
        NoOfPassengers = noOfPassengers;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getTotalDistance() {
        return TotalDistance;
    }

    public void setTotalDistance(String totalDistance) {
        TotalDistance = totalDistance;
    }

    public String getStartLat() {
        return StartLat;
    }

    public void setStartLat(String startLat) {
        StartLat = startLat;
    }

    public String getStartLong()
    {
        return StartLong;
    }

    public void setStartLong(String startLong) {
        StartLong = startLong;
    }

    public String getEndLat() {
        return EndLat;
    }

    public void setEndLat(String endLat) {
        EndLat = endLat;
    }

    public String getEndLong() {
        return EndLong;
    }

    public void setEndLong(String endLong) {
        EndLong = endLong;
    }

    private  String DateTime;
    private  String Kind;
    private  String NoOfPassengers;
    private  String Address;
    private  String Price;
    private  String TotalDistance;
    private  String StartLat;
    private  String StartLong;
    private  String EndLat;
    private  String EndLong;



}
