
package com.rajasahabacademy.activity.home.model.live_class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class UserDetails {

    @SerializedName("complated")
    @Expose
    private List<Complated> complated = null;
    @SerializedName("upcoming")
    @Expose
    private List<Upcoming> upcoming = null;
    @SerializedName("oncoming")
    @Expose
    private List<Oncoming> oncoming = null;

    public List<Complated> getComplated() {
        return complated;
    }

    public void setComplated(List<Complated> complated) {
        this.complated = complated;
    }

    public List<Upcoming> getUpcoming() {
        if (upcoming == null)
            return new ArrayList<>();
        return upcoming;
    }

    public void setUpcoming(List<Upcoming> upcoming) {
        this.upcoming = upcoming;
    }

    public List<Oncoming> getOncoming() {
        return oncoming;
    }

    public void setOncoming(List<Oncoming> oncoming) {
        this.oncoming = oncoming;
    }

}
