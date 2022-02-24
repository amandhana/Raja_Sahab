
package com.rajasahabacademy.activity.home.model.latest_course;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Course {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("expire")
    @Expose
    private String expire;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("vendor_name")
    @Expose
    private String vendorName;
    @SerializedName("course_buy_status")
    @Expose
    private String courseBuyStatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        if (title == null)
            return "null";
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getExpire() {
        if (expire == null)
            return "0";
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    public String getPrice() {
        if (price == null)
            return "0";
        else if (price.equals("0.00") || price.equals("0.0")) {
            int priceD = (int) Double.parseDouble(price);
            return String.valueOf(priceD);
        }
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        if (thumbnail == null)
            return "";
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getCourseBuyStatus() {
        if (courseBuyStatus == null)
            return "0";
        return courseBuyStatus;
    }

    public void setCourseBuyStatus(String courseBuyStatus) {
        this.courseBuyStatus = courseBuyStatus;
    }

}
