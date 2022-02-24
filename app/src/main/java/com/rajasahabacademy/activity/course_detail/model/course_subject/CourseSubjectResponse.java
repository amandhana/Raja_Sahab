
package com.rajasahabacademy.activity.course_detail.model.course_subject;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rajasahabacademy.activity.home.model.latest_course.Course;

public class CourseSubjectResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("ebook_category")
    @Expose
    private List<EbookCategory> ebookCategory = null;
    @SerializedName("videos_category")
    @Expose
    private List<VideosCategory> videosCategory = null;

    @SerializedName("course")
    @Expose
    private Course course;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<EbookCategory> getEbookCategory() {
        return ebookCategory;
    }

    public void setEbookCategory(List<EbookCategory> ebookCategory) {
        this.ebookCategory = ebookCategory;
    }

    public List<VideosCategory> getVideosCategory() {
        return videosCategory;
    }

    public void setVideosCategory(List<VideosCategory> videosCategory) {
        this.videosCategory = videosCategory;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
