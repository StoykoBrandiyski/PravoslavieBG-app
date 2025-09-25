package com.example.pravoslaviebg.models.search;

public class ResultItem {
    public enum Type { SAINT, MONASTERY, PRAYER, Book}

    public Type type;
    public int id;
    public String title;
    public String locationOrOther;

    public ResultItem(Type type, int id, String title, String locationOrOther) {
        this.type = type;
        this.id = id;
        this.title = title;
        this.locationOrOther = locationOrOther;
    }
}
