package com.smartstay.service;

import com.smartstay.dao.BillDAO;
import com.smartstay.interfaces.BillingOperations;
import com.smartstay.model.Bill;

import java.sql.SQLException;

public class BillingService implements BillingOperations {

    private final BillDAO billDAO;

    public BillingService() {
        billDAO = new BillDAO();
    }

    @Override
    public double calculateTotal(Bill bill) {

        double total =
                bill.getRoomCharges()
                        + bill.getAdditionalCharges()
                        - bill.getDiscount();

        return Math.max(total, 0);
    }

    @Override
    public Bill generateBill(
            int bookingId,
            double roomCharges,
            double additionalCharges,
            double discount
    ) {

        try {

            Bill bill = new Bill();

            bill.setBookingId(bookingId);
            bill.setRoomCharges(roomCharges);
            bill.setAdditionalCharges(
                    additionalCharges
            );
            bill.setDiscount(discount);

            double total = calculateTotal(bill);

            bill.setTotalAmount(total);
            bill.setPaymentStatus("Pending");

            billDAO.addBill(bill);

            return bill;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Could not generate bill: "
                            + e.getMessage(),
                    e
            );
        }
    }

    public void markAsPaid(int bookingId)
            throws SQLException {

        billDAO.updatePaymentStatus(
                bookingId,
                "Paid"
        );
    }
}