package com.mysurgery.object;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 4/17/2017.
 */

public class OptionsObj {
    @SerializedName("content")
    private String content;
    @SerializedName("url_option")
    private String url_option;

    public void setContent(String content) {
        this.content = content;
    }

    public void setUrl_option(String url_option) {
        this.url_option = url_option;
    }

    public String getContent() {
        return content;
    }

    public String getUrl_option() {
        return url_option;
    }
}
