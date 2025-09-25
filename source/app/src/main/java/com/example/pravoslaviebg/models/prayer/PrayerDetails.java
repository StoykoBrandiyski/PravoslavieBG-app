package com.example.pravoslaviebg.models.prayer;

public class PrayerDetails {
    private int Id;
    private String Title;
    private String ImageUrl;
    private String Saint;
    private String Purpose;
    private String Content;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getSaint() {
        return Saint;
    }

    public void setSaint(String saint) {
        Saint = saint;
    }

    public String getPurpose() {
        return Purpose;
    }

    public void setPurpose(String purpose) {
        Purpose = purpose;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
