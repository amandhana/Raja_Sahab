
package com.rajasahabacademy.activity.quiz.model.rank;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RankResponse {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("notification")
    @Expose
    private String notification;
    @SerializedName("ranking")
    @Expose
    private List<Ranking> ranking = null;
    @SerializedName("user_result")
    @Expose
    private UserResult userResult;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public List<Ranking> getRanking() {
        return ranking;
    }

    public void setRanking(List<Ranking> ranking) {
        this.ranking = ranking;
    }

    public UserResult getUserResult() {
        return userResult;
    }

    public void setUserResult(UserResult userResult) {
        this.userResult = userResult;
    }

}
