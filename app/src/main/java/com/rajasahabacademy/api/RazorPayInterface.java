package com.rajasahabacademy.api;

public interface RazorPayInterface {
    void onPaymentSuccess(String paymentId);

    void onPaymentError(int i, String s);
}
