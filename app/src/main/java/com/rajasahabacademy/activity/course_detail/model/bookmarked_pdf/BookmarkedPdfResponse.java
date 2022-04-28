
package com.rajasahabacademy.activity.course_detail.model.bookmarked_pdf;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookmarkedPdfResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Datum> getData() {
        if (data == null)
            return new ArrayList<>();
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

}
