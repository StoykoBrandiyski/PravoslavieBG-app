package com.example.pravoslaviebg.models;

public class Saint {
    public int Id;
    public String Name;
    public String description;
    public String iconUrl;

    public String MainImageUrl;
    public String SaintMind;

    public Saint(int id, String name, String description, String iconUrl) {
        this.Id = id;
        this.Name = name;
        this.description = description;
        this.iconUrl = iconUrl;
    }
    public int getId() {
        return Id;
    }
    public String getName() {
        return Name;
    }
    public String getMainImageUrl() {
        return MainImageUrl;
    }
    public String getSaintMind() {
        return SaintMind;
    }
}
