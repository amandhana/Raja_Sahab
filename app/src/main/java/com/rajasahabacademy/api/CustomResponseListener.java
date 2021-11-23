package com.rajasahabacademy.api;


public interface CustomResponseListener {
    void onResponse(int requestCode, String response);
    void onFailure(int statusCode, Throwable error);
}
