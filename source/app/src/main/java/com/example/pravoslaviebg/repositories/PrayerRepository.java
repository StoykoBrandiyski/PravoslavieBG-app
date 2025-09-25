package com.example.pravoslaviebg.repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.pravoslaviebg.models.prayer.Prayer;
import com.example.pravoslaviebg.models.prayer.PrayerDetails;
import com.example.pravoslaviebg.network.ApiService;
import com.example.pravoslaviebg.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrayerRepository {
    private final ApiService apiService;

    public PrayerRepository() {
        this.apiService = RetrofitClient.getApiService();
    }

    public void getPrayers(MutableLiveData<List<Prayer>> prayersLiveData) {
        apiService.getPrayers().enqueue(new Callback<List<Prayer>>() {
            @Override
            public void onResponse(Call<List<Prayer>> call, Response<List<Prayer>> response) {
                if (response.isSuccessful()) {
                    prayersLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Prayer>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void getPrayerDetails(int id, MutableLiveData<PrayerDetails> detailsLiveData) {
        apiService.getPrayerDetails(id).enqueue(new Callback<PrayerDetails>() {
            @Override
            public void onResponse(Call<PrayerDetails> call, Response<PrayerDetails> response) {
                if (response.isSuccessful()) {
                    detailsLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<PrayerDetails> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void getPrayersByFilter(
            MutableLiveData<List<Prayer>> prayersLiveData,
            String filters,
            String values
    ) {
        apiService.getPrayersByFilters(filters, values).enqueue(new Callback<List<Prayer>>() {
            @Override
            public void onResponse(Call<List<Prayer>> call, Response<List<Prayer>> response) {
                if (response.isSuccessful()) {
                    prayersLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Prayer>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
