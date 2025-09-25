package com.example.pravoslaviebg.repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.pravoslaviebg.models.monastery.Monastery;
import com.example.pravoslaviebg.models.monastery.MonasteryDetails;
import com.example.pravoslaviebg.network.ApiService;
import com.example.pravoslaviebg.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MonasteryRepository {
    private final ApiService apiService;

    public MonasteryRepository() {
        this.apiService = RetrofitClient.getApiService();
    }

    public void getRandomMonasteries(MutableLiveData<List<Monastery>> randomMonasteryLiveData) {
        apiService.getRandomMonasteries().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Monastery>> call, Response<List<Monastery>> response) {
                if (response.isSuccessful()) {
                    randomMonasteryLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Monastery>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void getMonasteries(MutableLiveData<List<Monastery>> monasteriesLiveData) {
        apiService.getMonasteries().enqueue(new Callback<List<Monastery>>() {
            @Override
            public void onResponse(Call<List<Monastery>> call, Response<List<Monastery>> response) {
                if (response.isSuccessful()) {
                    monasteriesLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Monastery>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void getMonasteryDetails(int id, MutableLiveData<MonasteryDetails> detailsLiveData) {
        apiService.getMonasteryDetails(id).enqueue(new Callback<MonasteryDetails>() {
            @Override
            public void onResponse(Call<MonasteryDetails> call, Response<MonasteryDetails> response) {
                if (response.isSuccessful()) {
                    detailsLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<MonasteryDetails> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void getMonasteriesByFilters(
            MutableLiveData<List<Monastery>> monasteriesLiveData,
            String filters,
            String values
    ) {
        apiService.getMonasteriesByFilters(filters, values).enqueue(new Callback<List<Monastery>>() {
            @Override
            public void onResponse(Call<List<Monastery>> call, Response<List<Monastery>> response) {
                if (response.isSuccessful()) {
                    monasteriesLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Monastery>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
