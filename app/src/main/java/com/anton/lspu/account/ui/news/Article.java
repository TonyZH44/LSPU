package com.anton.lspu.account.ui.news;

public class Article {

    private String url, title, description, date;
    private byte[] imageBinaryData;

    public Article(String url, String title, String description, byte[] imageBinaryData, String date){
        this.url = url;
        this.title = title;
        this.description = description;
        this.imageBinaryData = imageBinaryData;
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public byte[] getImageBinaryData() {
        return imageBinaryData;
    }

    public String getDate(){
        return date;
    }

}
