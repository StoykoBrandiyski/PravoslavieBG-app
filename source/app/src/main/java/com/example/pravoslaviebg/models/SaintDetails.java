package com.example.pravoslaviebg.models;

import com.example.pravoslaviebg.models.book.Book;
import com.example.pravoslaviebg.models.monastery.Monastery;
import com.example.pravoslaviebg.models.prayer.Prayer;

import java.util.List;

public class SaintDetails {
    private int Id;
    private String Name;
    private String Description;
    private String HelpFor;
    private String Type;
    private String LifePeriod;
    private String CelebrationDate;
    private List<String> ImageUrls;
    private List<Book> Books;
    private List<Prayer> Prayers;
    private List<Monastery> Monasteries;

    public int getId() { return Id; }
    public String getName() { return Name; }
    public String getDescription() { return Description; }
    public String getHelpFor() { return HelpFor; }
    public String getType() { return Type; }
    public String getLifePeriod() { return LifePeriod; }
    public String getCelebrationDate() { return CelebrationDate; }
    public List<String> getImageUrls() { return ImageUrls; }
    public List<Book> getBooks() { return Books; }
    public List<Prayer> getPrayers() { return Prayers; }
    public List<Monastery> getMonasteries() { return Monasteries; }
}
