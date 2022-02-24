
package com.rajasahabacademy.activity.chat.model.chat_update;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatUpdateResponse {

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
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

}
