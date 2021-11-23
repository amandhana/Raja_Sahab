
package com.rajasahabacademy.model.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("created_by")
    @Expose
    private String createdBy;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("course_buy_status")
    @Expose
    private String courseBuyStatus;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("course_id")
    @Expose
    private String courseId;
    @SerializedName("is_type")
    @Expose
    private String isType;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("quiz_id")
    @Expose
    private String quizId;

    public String getCreated() {
        if (created == null)
            return "";
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("read")
    @Expose
    private String read;
    @SerializedName("student_id")
    @Expose
    private String studentId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        if (subject == null)
            return "Empty Title";
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        if (message == null)
            return "Empty message";
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedAt() {
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

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseBuyStatus() {
        if (courseBuyStatus == null)
            return "0";
        return courseBuyStatus;
    }

    public void setCourseBuyStatus(String courseBuyStatus) {
        this.courseBuyStatus = courseBuyStatus;
    }

    public String getPrice() {
        if (price == null)
            return "0";
        else if (price.equals("0.00") || price.equals("0.0")) {
            int priceD = (int) Double.parseDouble(price);
            return String.valueOf(priceD);
        }
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getThumbnail() {
        if (thumbnail == null)
            return "";
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        if (title == null)
            return "null";
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCourseId() {
        if (courseId == null)
            return "";
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getIsType() {
        return isType;
    }

    public void setIsType(String isType) {
        this.isType = isType;
    }

    public String getDuration() {
        if (duration == null)
            return "0";
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getQuizId() {
        if (quizId == null)
            return "";
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }
}
