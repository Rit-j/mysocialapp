package com.example.mysocialapp.model;

public class filemodel
{
    String title,url;
    String date,time;

    public filemodel() {
    }

    public filemodel(String title, String url, String date, String time) {
        this.title = title;
        this.url = url;
        this.date = date;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
