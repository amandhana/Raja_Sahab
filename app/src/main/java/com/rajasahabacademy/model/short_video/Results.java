
package com.rajasahabacademy.model.short_video;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Results {

    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public List<Datum> getData() {
        if (data == null)
            return new ArrayList<>();
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

}
