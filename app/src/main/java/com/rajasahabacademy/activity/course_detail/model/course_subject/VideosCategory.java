
package com.rajasahabacademy.activity.course_detail.model.course_subject;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VideosCategory {

    @SerializedName("subject_id")
    @Expose
    private String subjectId;
    @SerializedName("name")
    @Expose
    private String name;

    public String getSubjectId() {
        if (subjectId == null)
            return "";
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getName() {
        if (name == null)
            return "null";
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
