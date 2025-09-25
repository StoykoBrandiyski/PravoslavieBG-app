package com.example.pravoslaviebg.models.monastery;

import java.util.List;

public class MonasteryDetails {
    private int Id;
    private String Name;
    private String Location;
    private String Description;
    private List<String> ImageUrls;
    private String Area;
    private String SaintName;
    private String Type;
    private String SaintId;

    public int getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public String getLocation() {
        return Location;
    }

    public List<String> getImageUrls() {
        return ImageUrls;
    }

    public String getArea() {
        return Area;
    }

    public String getSaintName() {
        return SaintName;
    }

    public String getType() {
        return Type;
    }

    public String getSaintId() {
        return SaintId;
    }

    public String getDescription() {
        return Description;
    }
}
