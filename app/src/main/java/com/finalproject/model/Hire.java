package com.finalproject.model;


public class Hire {

    String id,hiredUserId, userid, hiredName,name, fromDate, toDate;

    public Hire(String id, String hiredUserId, String userid, String hiredName, String name, String fromDate, String toDate) {
        this.id = id;
        this.hiredUserId = hiredUserId;
        this.userid = userid;
        this.hiredName = hiredName;
        this.name = name;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public Hire() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHiredUserId() {
        return hiredUserId;
    }

    public void setHiredUserId(String hiredUserId) {
        this.hiredUserId = hiredUserId;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getHiredName() {
        return hiredName;
    }

    public void setHiredName(String hiredName) {
        this.hiredName = hiredName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
}
