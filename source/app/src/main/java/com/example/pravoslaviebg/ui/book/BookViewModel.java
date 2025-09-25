package com.example.pravoslaviebg.ui.book;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pravoslaviebg.models.book.Book;
import com.example.pravoslaviebg.repositories.BookRepository;

import java.util.ArrayList;
import java.util.List;

public class BookViewModel extends ViewModel {

    private final BookRepository bookRepository;
    private Book selectedBook;
    public MutableLiveData<List<Book>> booksLiveData = new MutableLiveData<>();


    public BookViewModel() {
        this.bookRepository = new BookRepository();
    }


    public List<Book> getBooks() {
        return new ArrayList<>();
    }

    public void selectBook(Book book) {
        selectedBook = book;
    }

    public Book getSelectedBook() {
        return selectedBook;
    }

    public void fetchBooks() {
        this.bookRepository.getBooks(booksLiveData);
    }

}