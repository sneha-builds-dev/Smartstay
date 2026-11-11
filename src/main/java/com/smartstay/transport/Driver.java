package com.smartstay.transport;

public class Driver {

    private int driverId;
    private String driverName;
    private String phone;
    private String licenseNumber;
    private String status;

    public Driver() {
    }

    public Driver(int driverId, String driverName, String phone,
                  String licenseNumber, String status) {
        this.driverId = driverId;
        this.driverName = driverName;
        this.phone = phone;
        this.licenseNumber = licenseNumber;
        this.status = status;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    @Override
    public String toString() {
        return driverName;
    }
}