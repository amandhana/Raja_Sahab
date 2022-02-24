
package com.rajasahabacademy.activity.home.model.notes;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotesResponse {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("results")
    @Expose
    private List<ResultNotes> results = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ResultNotes> getResults() {
        return results;
    }

    public void setResults(List<ResultNotes> resultNotes) {
        this.results = resultNotes;
    }

}
