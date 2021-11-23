
package com.rajasahabacademy.model.rank;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rajasahabacademy.model.attempt_quiz.Question;

import java.util.List;

public class UserResult {

    @SerializedName("ranking")
    @Expose
    private String ranking;
    @SerializedName("percentage")
    @Expose
    private String percentage;

    public String getPercentage() {
        if (percentage == null)
            return "0";
        else if (percentage.contains(".")) {
            int index = percentage.indexOf(".");
            String beforePointStr = percentage.substring(0, index);
            if (beforePointStr.length() == 3)
                return beforePointStr;
            return percentage;
        }
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("questions_attempted")
    @Expose
    private String questionsAttempted;
    @SerializedName("correct")
    @Expose
    private String correct;
    @SerializedName("wrong")
    @Expose
    private String wrong;
    @SerializedName("skipped")
    @Expose
    private String skipped;
    @SerializedName("total_question")
    @Expose
    private String totalQuestion;

    @SerializedName("questions")
    @Expose
    private List<Question> questions = null;

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public String getRanking() {
        if (ranking == null)
            return "";
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionsAttempted() {
        return questionsAttempted;
    }

    public void setQuestionsAttempted(String questionsAttempted) {
        this.questionsAttempted = questionsAttempted;
    }

    public String getCorrect() {
        if (correct == null)
            return "0";
        return correct;
    }

    public void setCorrect(String correct) {
        this.correct = correct;
    }

    public String getWrong() {
        if (wrong == null)
            return "0";
        return wrong;
    }

    public void setWrong(String wrong) {
        this.wrong = wrong;
    }

    public String getSkipped() {
        if (skipped == null)
            return "0";
        return skipped;
    }

    public void setSkipped(String skipped) {
        this.skipped = skipped;
    }

    public String getTotalQuestion() {
        return totalQuestion;
    }

    public void setTotalQuestion(String totalQuestion) {
        this.totalQuestion = totalQuestion;
    }

}
