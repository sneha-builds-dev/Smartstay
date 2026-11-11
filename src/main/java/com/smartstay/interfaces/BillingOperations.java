package com.smartstay.interfaces;

import com.smartstay.model.Bill;

public interface BillingOperations {

    Bill generateBill(int bookingId,
                      double roomCharges,
                      double additionalCharges,
                      double discount);

    double calculateTotal(Bill bill);
}