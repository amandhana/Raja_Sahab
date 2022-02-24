
package com.rajasahabacademy.activity.quiz.model.question_ans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {
    int timeTakenInSeconds = 0;
    boolean isSelectFlag = false;
    boolean isSkipFlag = false;
    boolean isVisitedFlag = false;
    String selectedOption = "";

    public void setTimeTakenInSeconds(int timeTakenInSeconds) {
        this.timeTakenInSeconds = timeTakenInSeconds;
    }

    public int getTimeTakenInSeconds() {
        return timeTakenInSeconds;
    }

    public boolean getSelectFlag() {
        return isSelectFlag;
    }

    public void setSelectFlag(boolean selectFlag) {
        isSelectFlag = selectFlag;
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }

    public boolean getSkipFlag() {
        return isSkipFlag;
    }

    public void setSkipFlag(boolean skipFlag) {
        isSkipFlag = skipFlag;
    }

    public boolean getVisitedFlag() {
        return isVisitedFlag;
    }

    public void setVisitedFlag(boolean visitedFlag) {
        isVisitedFlag = visitedFlag;
    }

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("bookmark")
    @Expose
    private String bookmark;
    @SerializedName("test_id")
    @Expose
    private String testId;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("option1")
    @Expose
    private String option1;
    @SerializedName("option2")
    @Expose
    private String option2;
    @SerializedName("option3")
    @Expose
    private String option3;
    @SerializedName("option4")
    @Expose
    private String option4;
    @SerializedName("time_limit")
    @Expose
    private String timeLimit;
    @SerializedName("minus_mark")
    @Expose
    private String minusMark;
    @SerializedName("mark")
    @Expose
    private String mark;
    @SerializedName("ans")
    @Expose
    private String ans;
    @SerializedName("summary")
    @Expose
    private String summary;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getQuestion() {
        if (question == null)
            return "Empty Question";
        return question;
    }

    public String getBookmark() {
        if (bookmark == null)
            return "";
        return bookmark;
    }

    public void setBookmark(String bookmark) {
        this.bookmark = bookmark;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption1() {
        if (option1 == null)
            return "";
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        if (option2 == null)
            return "";
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        if (option3 == null)
            return "";
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        if (option4 == null)
            return "";
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public String getTimeLimit() {
        if (timeLimit == null)
            return "0";
        return timeLimit;
    }

    public void setTimeLimit(String timeLimit) {
        this.timeLimit = timeLimit;
    }

    public String getMinusMark() {
        return minusMark;
    }

    public void setMinusMark(String minusMark) {
        this.minusMark = minusMark;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

}
