package com.example.pravoslaviebg.network;

import android.content.Context;
import android.widget.Toast;

import com.bumptech.glide.load.engine.Resource;
import com.example.pravoslaviebg.LoginActivity;
import com.example.pravoslaviebg.RegisterActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Response;

import java.io.IOException;

public class ClientHandler<T> {

    public void errorRequest(Context context, Response<String> response) {
        if (response.code() == 400) {
            Toast.makeText(context, "Неуспешен вход. Проверете данните си.", Toast.LENGTH_SHORT).show();
        } else if (response.body() != null) {
            try {
                JSONObject jsonObject = new JSONObject(response.body());
                JSONArray errorsArray = jsonObject.getJSONArray("Errors");

                StringBuilder errorMessage = new StringBuilder();
                for (int i = 0; i < errorsArray.length(); i++) {
                    errorMessage.append(errorsArray.getString(i)).append("\n");
                }

                Toast.makeText(context, errorMessage.toString().trim(), Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }}
}
