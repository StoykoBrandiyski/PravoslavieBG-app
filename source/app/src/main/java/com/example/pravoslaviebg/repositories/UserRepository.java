package com.example.pravoslaviebg.repositories;

import android.util.Log;

import com.example.pravoslaviebg.models.User;
import com.example.pravoslaviebg.models.callbacks.UserDetailsCallback;
import com.example.pravoslaviebg.models.callbacks.UserUpdateCallback;
import com.example.pravoslaviebg.network.ApiService;
import com.example.pravoslaviebg.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private final ApiService apiService;

    public UserRepository() {
        this.apiService = RetrofitClient.getApiService();
    }

    public void getUserDetails(String token, UserDetailsCallback callback)  {
        RetrofitClient.getClient(token)
                .create(ApiService.class)
                .getUserDetails().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else if (response.code() == 404) {
                    callback.onError("User not found");
                } else if (response.code() == 401) {
                    callback.onAuthError();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("user_repo", t.getMessage() != null ? t.getMessage() : "");
            }
        });
    }

    public void updateUser(String token, User updatedUser, UserUpdateCallback callback) {
        apiService.updateUser(token, updatedUser).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    callback.onResult(true);
                }else if (response.code() == 404) {
                    callback.onResult(false);
                    callback.onError("User not found");
                } else if (response.code() == 401) {
                    callback.onAuthError();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                callback.onResult(false);
                Log.e("UserUpdate",  t.getMessage() != null ? t.getMessage() : "");
            }
        });
    }

}
