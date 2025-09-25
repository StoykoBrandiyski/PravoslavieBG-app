package com.example.pravoslaviebg.repositories;

import com.example.pravoslaviebg.models.quote.QuoteDaily;
import com.example.pravoslaviebg.network.ApiService;
import com.example.pravoslaviebg.network.RetrofitClient;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;

public class QuoteRepository {

    private final ApiService apiService;

    public QuoteRepository() {
        this.apiService = RetrofitClient.getApiService();
    }

    public Response<QuoteDaily> getSaintDailyNotificationSync() {
        Response<QuoteDaily> response = null;
        try {
            response = apiService.quoteDaily().execute();
        } catch (IOException e) {}

        return response;
    }
}
