
package com.rajasahabacademy.model.chat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rajasahabacademy.support.Utils;

public class Result {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("sender_id")
    @Expose
    private String senderId;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("sent_to_user")
    @Expose
    private String sentToUser;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenderId() {
        if (senderId == null)
            return "";
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        if (message == null)
            return "null";
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatedAt() {
        if (createdAt == null)
            return Utils.getCurrentDate();
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getSentToUser() {
        return sentToUser;
    }

    public void setSentToUser(String sentToUser) {
        this.sentToUser = sentToUser;
    }

}
