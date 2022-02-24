
package com.rajasahabacademy.activity.quiz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultLiveQuiz {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("course_id")
    @Expose
    private String courseId;
    @SerializedName("result_time")
    @Expose
    private String resultTime;
    @SerializedName("test_date_time")
    @Expose
    private String testDateTime;
    @SerializedName("no_of_question")
    @Expose
    private String noOfQuestion;
    @SerializedName("marks")
    @Expose
    private String marks;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("negative_marking")
    @Expose
    private String negativeMarking;
    @SerializedName("entry_time_max")
    @Expose
    private Object entryTimeMax;
    @SerializedName("entry_time_registration")
    @Expose
    private String entryTimeRegistration;
    @SerializedName("attended")
    @Expose
    private String attended;

    public String getId() {
        if (id == null)
            return "";
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        if (title == null)
            return "Empty Title";
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStatus() {
        if (status == null)
            return "0";
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        if (createdAt == null)
            return "Empty";
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getResultTime() {
        return resultTime;
    }

    public void setResultTime(String resultTime) {
        this.resultTime = resultTime;
    }

    public String getTestDateTime() {
        return testDateTime;
    }

    public void setTestDateTime(String testDateTime) {
        this.testDateTime = testDateTime;
    }

    public String getNoOfQuestion() {
        if (noOfQuestion == null)
            return "0";
        return noOfQuestion;
    }

    public void setNoOfQuestion(String noOfQuestion) {
        this.noOfQuestion = noOfQuestion;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public String getDuration() {
        if (duration == null)
            return "0";
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getNegativeMarking() {
        return negativeMarking;
    }

    public void setNegativeMarking(String negativeMarking) {
        this.negativeMarking = negativeMarking;
    }

    public Object getEntryTimeMax() {
        return entryTimeMax;
    }

    public void setEntryTimeMax(Object entryTimeMax) {
        this.entryTimeMax = entryTimeMax;
    }

    public String getEntryTimeRegistration() {
        return entryTimeRegistration;
    }

    public void setEntryTimeRegistration(String entryTimeRegistration) {
        this.entryTimeRegistration = entryTimeRegistration;
    }

    public String getAttended() {
        if (attended == null)
            return "0";
        return attended;
    }

    public void setAttended(String attended) {
        this.attended = attended;
    }

}
