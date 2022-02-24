
package com.rajasahabacademy.activity.course_detail.model.course_video;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("bookmark")
    @Expose
    private String bookmark;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("tags")
    @Expose
    private Object tags;
    @SerializedName("demo")
    @Expose
    private Object demo;

    public Object getDemo() {
        if (demo == null)
            return "0";
        return demo;
    }

    public String getBookmark() {
        if (bookmark == null)
            return "";
        return bookmark;
    }

    public void setBookmark(String bookmark) {
        this.bookmark = bookmark;
    }

    public void setDemo(Object demo) {
        this.demo = demo;
    }

    @SerializedName("path")
    @Expose
    private String path;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("deleted")
    @Expose
    private String deleted;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("course_id")
    @Expose
    private String courseId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        if (title == null)
            return "No Title";
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Object getTags() {
        return tags;
    }

    public void setTags(Object tags) {
        this.tags = tags;
    }

    public String getPath() {
        if (path == null)
            return "";
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getThumbnail() {
        if (thumbnail == null)
            return "";
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

    public String getDescription() {
        if (description == null)
            return "";
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
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

    public String getType() {
        if (type == null)
            return "";
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

}
