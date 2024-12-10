package com.example.a17;

public class Product {
    private String id;
    private String title;
    private String author;
    private String description;
    private String photoPath;

    public Product(String id, String title, String author, String description, String photoPath) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
        this.photoPath = photoPath;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getPhotoPath() { return photoPath; }
    public void setPhotoPath(String photoPath) { this.photoPath = photoPath; }
}
