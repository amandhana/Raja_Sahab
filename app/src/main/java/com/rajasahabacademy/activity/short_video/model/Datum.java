
package com.rajasahabacademy.activity.short_video.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    boolean flag = false;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("video")
    @Expose
    private String video;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("path")
    @Expose
    private String path;
    @SerializedName("like")
    @Expose
    private Integer like;

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

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
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

    public String getPath() {
        if (path == null)
            return "";
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getLike() {
        if (like == null)
            return 0;
        return like;
    }

    public void setLike(Integer like) {
        this.like = like;
    }

}
