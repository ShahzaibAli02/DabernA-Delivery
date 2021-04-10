package com.daber.daberna.Model;

public class DriverRegistrationModel
{


    /*

    Driver Registration Model Class

     */

    private  String DriverName;
    private  String Email;
    private  String Password;
    private  String DriverCarType;
    private  String DriverCarPlateNo;
    private  String DriverStars;
    private  String TotalCustomers;
    private  String ServiceStars;

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

    private  String Phone;

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }





    public  DriverRegistrationModel ()
    {
        setDriverStars("5");
        setServiceStars("5");
        setTotalCustomers("0");
    }
    public String getTotalCustomers() {
        return TotalCustomers;
    }

    public void setTotalCustomers(String totalCustomers) {
        TotalCustomers = totalCustomers;
    }





    public String getDriverStars() {
        return DriverStars;
    }

    public void setDriverStars(String driverStars) {
        DriverStars = driverStars;
    }

    public String getServiceStars() {
        return ServiceStars;
    }

    public void setServiceStars(String serviceStars) {
        ServiceStars = serviceStars;
    }



    public String getDriverID() {
        return DriverID;
    }

    public void setDriverID(String driverID) {
        DriverID = driverID;
    }

    private  String DriverID;

    public String getDriverName() {
        return DriverName;
    }

    public void setDriverName(String driverName) {
        DriverName = driverName;
    }


    public String getDriverCarType() {
        return DriverCarType;
    }

    public void setDriverCarType(String driverCarType) {
        DriverCarType = driverCarType;
    }

    public String getDriverCarPlateNo() {
        return DriverCarPlateNo;
    }

    public void setDriverCarPlateNo(String driverCarPlateNo) {
        DriverCarPlateNo = driverCarPlateNo;
    }

    public Boolean getDriver() {
        return isDriver;
    }

    public void setDriver(Boolean driver) {
        isDriver = driver;
    }

    private  Boolean isDriver;


}
