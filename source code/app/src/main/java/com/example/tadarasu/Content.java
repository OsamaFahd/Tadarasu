package com.example.tadarasu;

public class Content {
    private String Title, Name, image, Category, PostID,
            Text, UserId, Sound, Video;



    public  Content () {

    }
    public Content(String title, String name, String img, String cat, String postID,
                   String text, String userId, String sound, String video) {
        this.Title = title;
        this.Name = name;
        this.image = img;
        this.Category = cat;
        this.PostID = postID;
        this.Text = text;
        this.UserId = userId;
        this.Sound = sound;
        this.Video = video;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImg(String img) {
        this.image = img;
    }

    public String getPostID() { return PostID; }

    public String getText() { return Text; }

    public String getUserId() { return UserId; }

    public String getSound() { return Sound; }

    public String getVideo() { return Video; }
}


