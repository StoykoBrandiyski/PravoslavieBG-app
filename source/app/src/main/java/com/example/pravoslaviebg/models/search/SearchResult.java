package com.example.pravoslaviebg.models.search;

import com.example.pravoslaviebg.models.book.Book;
import com.example.pravoslaviebg.models.monastery.Monastery;
import com.example.pravoslaviebg.models.Saint;
import com.example.pravoslaviebg.models.prayer.Prayer;

import java.util.ArrayList;
import java.util.List;

public class SearchResult {
    public List<Saint> Saints;
    public List<Monastery> Monasteries;
    public List<Book> Books;
    public List<Prayer> Prayers = new ArrayList<>();
}

