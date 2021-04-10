package com.daber.daberna.Model;

public class PassengerRegistrationModel
{



    /*

    Passenger Registration Model Class

     */

        private  String PassengerName;
        private  String Email;
         private  String Phone;
        private  String  Password;
        private  Boolean isDriver;


    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }



    public String getPassengerID()
    {
        return PassengerID;
    }

    public void setPassengerID(String passengerID)
    {
        PassengerID = passengerID;
    }

    private  String PassengerID;

    public Boolean getDriver() {
        return isDriver;
    }

    public void setDriver(Boolean driver) {
        isDriver = driver;
    }



    public String getPassengerName() {
        return PassengerName;
    }

    public void setPassengerName(String passengerName) {
        PassengerName = passengerName;
    }


    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }



}
