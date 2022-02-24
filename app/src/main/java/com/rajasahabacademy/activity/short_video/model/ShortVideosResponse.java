
package com.rajasahabacademy.activity.short_video.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShortVideosResponse {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("results")
    @Expose
    private Results results;

    public String getMessage() {
        if (message == null)
            return "";
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

}
