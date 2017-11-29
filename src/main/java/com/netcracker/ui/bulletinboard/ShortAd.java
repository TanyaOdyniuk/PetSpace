package com.netcracker.ui.bulletinboard;

import java.util.Date;

class ShortAd {
    private String topic;
    private String author;
    private String information;
    private Date date;
    private String status;

    public ShortAd(String topic, String author, String information, Date date, String status) {
        this.topic = topic;
        this.author = author;
        this.information = information;
        this.date = date;
        this.status = status;
    }

    public String getTopic() {
        return topic;
    }

    public String getAuthor() {
        return author;
    }

    public String getInformation() {
        return information;
    }

    public Date getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }
}
