
package com.rajasahabacademy.model.course.course_video;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CourseVideoResponse {

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
