package com.example.pravoslaviebg.repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.pravoslaviebg.models.book.Book;
import com.example.pravoslaviebg.network.ApiService;
import com.example.pravoslaviebg.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookRepository {
    private final ApiService apiService;

    public BookRepository() {
        this.apiService = RetrofitClient.getApiService();
    }

    public void getBooks(MutableLiveData<List<Book>> prayersLiveData) {
        apiService.getBooks().enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful()) {
                    prayersLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
