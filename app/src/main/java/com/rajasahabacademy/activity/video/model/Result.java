
package com.rajasahabacademy.activity.video.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rajasahabacademy.support.Utils;

public class Result {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("video_id")
    @Expose
    private String videoId;
    @SerializedName("from")
    @Expose
    private String from;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("name")
    @Expose
    private String name;

    public String getId() {
        if (id == null)
            return "";
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getFrom() {
        if (from == null)
            return "";
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        if (message == null)
            return "";
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

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        if (name == null)
            return "";
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
