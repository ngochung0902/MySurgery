package com.mysurgery.object;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 4/17/2017.
 */

public class TypeObject {
    @SerializedName("type_name")
    private String type_name;
    @SerializedName("days_type")
    private String days_type;
    @SerializedName("options")
    private List<OptionsObj> options;

    public void setOptions(List<OptionsObj> options) {
        this.options = options;
    }

    public List<OptionsObj> getOptions() {
        return options;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getType_name() {
        return type_name;
    }

    public void setDays_type(String days_type) {
        this.days_type = days_type;
    }

    public String getDays_type() {
        return days_type;
    }
}
