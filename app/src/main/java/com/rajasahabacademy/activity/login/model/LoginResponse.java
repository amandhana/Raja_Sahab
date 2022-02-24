
package com.rajasahabacademy.activity.login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("notification")
    @Expose
    private String notification;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("results")
    @Expose
    private Results results;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("email")
    @Expose
    private String email;

    public String getNotification() {
        if (notification == null)
            return "Successfully";
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getMessage() {
        if (message == null)
            return "User deactive !! Please active first";
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Results getResults() {
        return results;
    }

    public void setResults(Results results) {
        this.results = results;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
