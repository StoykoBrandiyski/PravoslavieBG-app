package com.example.pravoslaviebg.network;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;
    private static final String HOST = "http://192.168.0.103:";
    private static final String PORT = "5678";

    private RetrofitClient() {}

    public static Retrofit getClient(String token) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        if (token != null) {
            clientBuilder.addInterceptor(chain -> {
                Request request = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + token)
                        .build();
                return chain.proceed(request);
            });
        }

        return new Retrofit.Builder()
                .baseUrl(HOST + PORT)
                .addConverterFactory(GsonConverterFactory.create())
                .client(clientBuilder.build())
                .build();
    }

    public static ApiService getApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(HOST + PORT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit.create(ApiService.class);
    }
}
