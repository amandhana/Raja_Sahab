
package com.rajasahabacademy.model.attempt_quiz;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Question {

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
    @SerializedName("your_answer")
    @Expose
    private String yourAnswer;
    @SerializedName("corrent_answer")
    @Expose
    private String correntAnswer;

    public String getQuestion() {
        if (question == null)
            return "";
        return question;
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

    public String getYourAnswer() {
        if (yourAnswer == null)
            return "";
        return yourAnswer;
    }

    public void setYourAnswer(String yourAnswer) {
        this.yourAnswer = yourAnswer;
    }

    public String getCorrentAnswer() {
        if (correntAnswer == null)
            return "";
        return correntAnswer;
    }

    public void setCorrentAnswer(String correntAnswer) {
        this.correntAnswer = correntAnswer;
    }

}
