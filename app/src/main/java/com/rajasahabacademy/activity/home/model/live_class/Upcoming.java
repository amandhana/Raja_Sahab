
package com.rajasahabacademy.activity.home.model.live_class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Upcoming {

    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("zoom_api_key")
    @Expose
    private String zoomApiKey;
    @SerializedName("zoom_api_secret")
    @Expose
    private String zoomApiSecret;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("meeting_id")
    @Expose
    private String meetingId;
    @SerializedName("host_email")
    @Expose
    private String hostEmail;
    @SerializedName("h323_password")
    @Expose
    private String h323Password;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("class_name")
    @Expose
    private String className;

    public String getThumbnail() {
        if (thumbnail == null)
            return "";
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDescription() {
        if (description == null)
            return "";
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getZoomApiKey() {
        if (zoomApiKey == null)
            return "";
        return zoomApiKey;
    }

    public void setZoomApiKey(String zoomApiKey) {
        this.zoomApiKey = zoomApiKey;
    }

    public String getZoomApiSecret() {
        if (zoomApiSecret == null)
            return "";
        return zoomApiSecret;
    }

    public void setZoomApiSecret(String zoomApiSecret) {
        this.zoomApiSecret = zoomApiSecret;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMeetingId() {
        if (meetingId == null)
            return "";
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String getHostEmail() {
        return hostEmail;
    }

    public void setHostEmail(String hostEmail) {
        this.hostEmail = hostEmail;
    }

    public String getH323Password() {
        return h323Password;
    }

    public void setH323Password(String h323Password) {
        this.h323Password = h323Password;
    }

    public String getPassword() {
        if (password == null)
            return "";
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClassName() {
        if (className == null)
            return "";
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

}
