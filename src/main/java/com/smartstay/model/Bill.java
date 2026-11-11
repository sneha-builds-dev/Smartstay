package com.smartstay.model;

public class Bill {

    private int billId;
    private int bookingId;
    private double roomCharges;
    private double additionalCharges;
    private double discount;
    private double totalAmount;
    private String paymentStatus;

    public Bill() {
    }

    public Bill(int billId, int bookingId, double roomCharges,
                double additionalCharges, double discount,
                double totalAmount, String paymentStatus) {

        this.billId = billId;
        this.bookingId = bookingId;
        this.roomCharges = roomCharges;
        this.additionalCharges = additionalCharges;
        this.discount = discount;
        this.totalAmount = totalAmount;
        this.paymentStatus = paymentStatus;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public double getRoomCharges() {
        return roomCharges;
    }

    public void setRoomCharges(double roomCharges) {
        this.roomCharges = roomCharges;
    }

    public double getAdditionalCharges() {
        return additionalCharges;
    }

    public void setAdditionalCharges(double additionalCharges) {
        this.additionalCharges = additionalCharges;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}