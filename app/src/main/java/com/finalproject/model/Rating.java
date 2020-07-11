package com.finalproject.model;

public class Rating {

    String id, message, star, userId, givenUser, givenUserId;

    public Rating() {
    }

    public Rating(String id, String message, String star, String userId, String givenUser, String givenUserId) {
        this.id = id;
        this.message = message;
        this.star = star;
        this.userId = userId;
        this.givenUser = givenUser;
        this.givenUserId = givenUserId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGivenUser() {
        return givenUser;
    }

    public void setGivenUser(String givenUser) {
        this.givenUser = givenUser;
    }

    public String getGivenUserId() {
        return givenUserId;
    }

    public void setGivenUserId(String givenUserId) {
        this.givenUserId = givenUserId;
    }
}
