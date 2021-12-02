package com.example.chatmessage.models;

public class Users {
    private String id;
 private String name;
 private String email;
private String imageUri;
private long time;

public Users(){

}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Users(String name, String email, String imageUri, long time,String id) {
        this.name = name;
        this.id = id;
        this.email = email;
        this.imageUri = imageUri;
        this.time = time;
    }
}
