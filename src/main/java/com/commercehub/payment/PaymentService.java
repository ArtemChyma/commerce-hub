package com.commercehub.payment;

import com.commercehub.order.Order;

public class PaymentService {

    public Payment processPayment(Order order, boolean paymentApproved) {
        if (order == null) {
            throw new IllegalArgumentException("Order must not be null");
        }

        Payment payment = new Payment(order.getId(), order.getTotal());
        order.startPayment();

        if(paymentApproved) {
            order.markAsPaid();
            payment.markSucceeded();
        } else {
            order.markPaymentFailed();
            payment.markFailed();
        }
        return payment;
    }
}
