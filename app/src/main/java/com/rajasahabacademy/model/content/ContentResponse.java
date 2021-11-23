
package com.rajasahabacademy.model.content;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContentResponse {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("notification")
    @Expose
    private String notification;
    @SerializedName("about_us")
    @Expose
    private String aboutUs;
    @SerializedName("term_conditions")
    @Expose
    private String termConditions;
    @SerializedName("privacy")
    @Expose
    private String privacy;
    @SerializedName("refund")
    @Expose
    private String refund;
    @SerializedName("contact_us")
    @Expose
    private String contactUs;

    public String getContactUs() {
        if (contactUs == null)
            return "";
        return contactUs;
    }

    public void setContactUs(String contactUs) {
        this.contactUs = contactUs;
    }

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

    public String getAboutUs() {
        return aboutUs;
    }

    public void setAboutUs(String aboutUs) {
        this.aboutUs = aboutUs;
    }

    public String getTermConditions() {
        return termConditions;
    }

    public void setTermConditions(String termConditions) {
        this.termConditions = termConditions;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getRefund() {
        return refund;
    }

    public void setRefund(String refund) {
        this.refund = refund;
    }

}
