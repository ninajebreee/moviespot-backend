package com.example.moviespot.dto;
public class ReviewDTO {
    private String title; private String content; private Integer rating;
    public String getTitle() { return title; } public void setTitle(String t) { this.title = t; }
    public String getContent() { return content; } public void setContent(String c) { this.content = c; }
    public Integer getRating() { return rating; } public void setRating(Integer r) { this.rating = r; }
}
