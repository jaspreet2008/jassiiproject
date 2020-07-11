package com.finalproject.model;


public class Upload {

    String userId, url;

    public Upload() {
    }

    public Upload(String userId, String url) {
        this.userId = userId;
        this.url = url;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
