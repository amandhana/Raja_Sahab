
package com.rajasahabacademy.activity.profile.model.CityResponse;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CityResponse {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("notification")
    @Expose
    private String notification;
    @SerializedName("results")
    @Expose
    private List<ResultCity> results = null;

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

    public List<ResultCity> getResults() {
        if (results == null)
            return new ArrayList<>();
        return results;
    }

    public void setResults(List<ResultCity> results) {
        this.results = results;
    }

}
