package com.example.pravoslaviebg.repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.pravoslaviebg.models.game.question.QuestionModel;
import com.example.pravoslaviebg.network.ApiService;
import com.example.pravoslaviebg.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionRepository {
    private final ApiService apiService;

    public QuestionRepository() {
        this.apiService = RetrofitClient.getApiService();
    }

    public void getAllQuestions(MutableLiveData<List<QuestionModel>> questionsLiveData) {
        apiService.getQuestions().enqueue(new Callback<List<QuestionModel>>() {
            @Override
            public void onResponse(Call<List<QuestionModel>> call, Response<List<QuestionModel>> response) {
                if (response.isSuccessful()) {
                    questionsLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<QuestionModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
