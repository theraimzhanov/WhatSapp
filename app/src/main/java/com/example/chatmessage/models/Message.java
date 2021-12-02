package com.example.chatmessage.models;

public class Message {
    private String author;
    private String textOfMessage;
    private long date;
    private String imageUrl;
    private String profilUri;

    public String getProfilUri() {
        return profilUri;
    }

    public void setProfilUri(String profilUri) {
        this.profilUri = profilUri;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTextOfMessage() {
        return textOfMessage;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setTextOfMessage(String textOfMessage) {
        this.textOfMessage = textOfMessage;
    }

    public Message(String author, String textOfMessage, long date, String imageUrl,String profilUri) {
        this.author = author;
        this.textOfMessage = textOfMessage;
        this.profilUri=profilUri;
        this.date = date;
        this.imageUrl = imageUrl;
    }

    public Message() {
    }
}
