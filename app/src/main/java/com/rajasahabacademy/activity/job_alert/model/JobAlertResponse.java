
package com.rajasahabacademy.activity.job_alert.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class JobAlertResponse {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("notification")
    @Expose
    private String notification;
    @SerializedName("results")
    @Expose
    private List<Result> results = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNotification() {
        if (notification == null)
            return "";
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public List<Result> getResults() {
        if (results == null)
            return new ArrayList<>();
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

}
