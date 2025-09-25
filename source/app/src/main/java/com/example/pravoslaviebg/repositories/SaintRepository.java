package com.example.pravoslaviebg.repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.pravoslaviebg.models.Saint;
import com.example.pravoslaviebg.models.SaintDetails;
import com.example.pravoslaviebg.models.saint.SaintDailyMind;
import com.example.pravoslaviebg.network.ApiService;
import com.example.pravoslaviebg.network.RetrofitClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaintRepository {

    private final ApiService apiService;
    private final Map<Integer, SaintDetails> saintDetailsCache = new HashMap<>();

    public SaintRepository() {
        this.apiService = RetrofitClient.getApiService();
    }

    public void getSaints(MutableLiveData<List<Saint>> saintsLiveData) {
        apiService.getSaints().enqueue(new Callback<List<Saint>>() {
            @Override
            public void onResponse(Call<List<Saint>> call, Response<List<Saint>> response) {
                if (response.isSuccessful()) {
                    saintsLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Saint>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void getSaintDetails(int id, MutableLiveData<SaintDetails> detailsLiveData) {
        // Check cache first
        if (saintDetailsCache.containsKey(id)) {
            detailsLiveData.setValue(saintDetailsCache.get(id));
            return;
        }

        apiService.getSaintDetails(id).enqueue(new Callback<SaintDetails>() {
            @Override
            public void onResponse(Call<SaintDetails> call, Response<SaintDetails> response) {
                if (response.isSuccessful()) {
                    SaintDetails details = response.body();
                    saintDetailsCache.put(id, details);  // Save to cache
                    detailsLiveData.setValue(details);
                }
            }

            @Override
            public void onFailure(Call<SaintDetails> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void getSaintsByFilter(
            MutableLiveData<List<Saint>> saintsLiveData,
            String filters,
            String values
    ) {
        apiService.getSaintsByFilters(filters, values).enqueue(new Callback<List<Saint>>() {
            @Override
            public void onResponse(Call<List<Saint>> call, Response<List<Saint>> response) {
                if (response.isSuccessful()) {
                    saintsLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Saint>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public Response<List<SaintDailyMind>> getSaintDailyNotificationSync() {
        Response<List<SaintDailyMind>> response = null;
        try {
            response = apiService.saintDailyMind().execute();
        } catch (IOException e) {}

        return response;
    }
}
