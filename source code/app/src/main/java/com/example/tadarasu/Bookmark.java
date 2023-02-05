package com.example.tadarasu;

public class Bookmark {
    private String PostID;


    public  Bookmark () {

    }
    public Bookmark(String postID) {
        PostID = postID;
    }

    public String getPostID() {
        return PostID;
    }

    public void setPostID(String postID) {
        PostID = postID;
    }
}
