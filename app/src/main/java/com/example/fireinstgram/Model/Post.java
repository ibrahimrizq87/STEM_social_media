package com.example.fireinstgram.Model;

public class Post{
private String description ;
    private String imageuri ;
    private String postid ;
    private String publisher ;

    public Post() {
    }

    public Post(String description, String imageuri, String postid, String publisher) {
        this.description = description;
        this.imageuri = imageuri;
        this.postid = postid;
        this.publisher = publisher;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageuri() {
        return imageuri;
    }

    public void setImageuri(String imageuri) {
        this.imageuri = imageuri;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}