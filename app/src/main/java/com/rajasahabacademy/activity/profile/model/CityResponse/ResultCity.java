
package com.rajasahabacademy.activity.profile.model.CityResponse;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultCity {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;

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

}
