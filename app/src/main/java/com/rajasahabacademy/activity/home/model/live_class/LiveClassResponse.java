
package com.rajasahabacademy.activity.home.model.live_class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LiveClassResponse {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("userDetails")
    @Expose
    private com.rajasahabacademy.activity.home.model.live_class.UserDetails userDetails;

    public Integer getCode() {
        if (code == null)
            return 0;
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public com.rajasahabacademy.activity.home.model.live_class.UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(com.rajasahabacademy.activity.home.model.live_class.UserDetails userDetails) {
        this.userDetails = userDetails;
    }

}
