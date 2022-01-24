
package com.rajasahabacademy.model.profile_detail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Success {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("email_verified_at")
    @Expose
    private Object emailVerifiedAt;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("courses")
    @Expose
    private Object courses;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("device_id")
    @Expose
    private Object deviceId;
    @SerializedName("device_token")
    @Expose
    private Object deviceToken;
    @SerializedName("is_verified")
    @Expose
    private String isVerified;
    @SerializedName("otp")
    @Expose
    private Object otp;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("education")
    @Expose
    private String education;
    @SerializedName("ref_code")
    @Expose
    private String refCode;
    @SerializedName("wallet")
    @Expose
    private String wallet;

    public String getState() {
        if (state == null)
            return "";
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEducation() {
        if (education == null)
            return "";
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getCity() {
        if (city == null)
            return "";
        return city;
    }



    public void setCity(String city) {
        this.city = city;
    }

    @SerializedName("type")
    @Expose
    private Object type;
    @SerializedName("curr_token")
    @Expose
    private String currToken;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("c_password")
    @Expose
    private Object cPassword;
    @SerializedName("ip")
    @Expose
    private String ip;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        if (name == null)
            return "";
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        if (email == null)
            return "";
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        if (address == null)
            return "";
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        if (phone == null)
            return "null";
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Object getEmailVerifiedAt() {
        return emailVerifiedAt;
    }

    public void setEmailVerifiedAt(Object emailVerifiedAt) {
        this.emailVerifiedAt = emailVerifiedAt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Object getCourses() {
        return courses;
    }

    public void setCourses(Object courses) {
        this.courses = courses;
    }

    public String getImage() {
        if (image == null)
            return "";
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Object getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Object deviceId) {
        this.deviceId = deviceId;
    }

    public Object getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(Object deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(String isVerified) {
        this.isVerified = isVerified;
    }

    public Object getOtp() {
        return otp;
    }

    public void setOtp(Object otp) {
        this.otp = otp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Object getType() {
        return type;
    }

    public void setType(Object type) {
        this.type = type;
    }

    public String getCurrToken() {
        return currToken;
    }

    public void setCurrToken(String currToken) {
        this.currToken = currToken;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getRefCode() {
        if (refCode == null)
            return "null";
        return refCode;
    }

    public void setRefCode(String refCode) {
        this.refCode = refCode;
    }

    public Object getcPassword() {
        return cPassword;
    }

    public void setcPassword(Object cPassword) {
        this.cPassword = cPassword;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getWallet() {
        if (wallet == null)
            return "0.0";
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }
}
