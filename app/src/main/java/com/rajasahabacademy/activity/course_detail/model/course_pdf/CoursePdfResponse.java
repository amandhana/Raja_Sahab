
package com.rajasahabacademy.activity.course_detail.model.course_pdf;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CoursePdfResponse {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("results")
    @Expose
    private Results results;

    public String getMessage() {
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
