
package com.rajasahabacademy.activity.quiz.model.attempt_quiz;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("student_id")
    @Expose
    private String studentId;
    @SerializedName("test_id")
    @Expose
    private String testId;
    @SerializedName("total_time")
    @Expose
    private String totalTime;
    @SerializedName("total_marks")
    @Expose
    private String totalMarks;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("percentage")
    @Expose
    private String percentage;
    @SerializedName("ranking")
    @Expose
    private String ranking;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("result_time")
    @Expose
    private String resultTime;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("course_id")
    @Expose
    private String courseId;
    @SerializedName("test_date_time")
    @Expose
    private String testDateTime;
    @SerializedName("no_of_question")
    @Expose
    private String noOfQuestion;
    @SerializedName("marks")
    @Expose
    private String marks;
    @SerializedName("negative_marking")
    @Expose
    private String negativeMarking;
    @SerializedName("entry_time_max")
    @Expose
    private String entryTimeMax;
    @SerializedName("entry_time_registration")
    @Expose
    private String entryTimeRegistration;
    @SerializedName("attended")
    @Expose
    private String attended;
    @SerializedName("questions")
    @Expose
    private List<Question> questions = null;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(String totalMarks) {
        this.totalMarks = totalMarks;
    }

    public String getCreatedAt() {
        if (createdAt == null)
            return "";
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

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

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
            return "";
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getResultTime() {
        return resultTime;
    }

    public void setResultTime(String resultTime) {
        this.resultTime = resultTime;
    }

    public String getDuration() {
        if (duration == null)
            return "";
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getTestDateTime() {
        return testDateTime;
    }

    public void setTestDateTime(String testDateTime) {
        this.testDateTime = testDateTime;
    }

    public String getNoOfQuestion() {
        if (noOfQuestion == null)
            return "";
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

    public String getNegativeMarking() {
        return negativeMarking;
    }

    public void setNegativeMarking(String negativeMarking) {
        this.negativeMarking = negativeMarking;
    }

    public String getEntryTimeMax() {
        return entryTimeMax;
    }

    public void setEntryTimeMax(String entryTimeMax) {
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
            return "";
        return attended;
    }

    public void setAttended(String attended) {
        this.attended = attended;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

}
