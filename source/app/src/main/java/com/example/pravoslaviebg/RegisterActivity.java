package com.example.pravoslaviebg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pravoslaviebg.models.SimpleResponse;
import com.example.pravoslaviebg.network.ApiService;
import com.example.pravoslaviebg.network.ClientHandler;
import com.example.pravoslaviebg.network.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText nameInput, emailInput, passwordInput, repeatPasswordInput, birthDateInput;
    RadioGroup genderGroup;
    Button registerButton;

    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameInput = findViewById(R.id.editTextName);
        emailInput = findViewById(R.id.editTextEmail);
        passwordInput = findViewById(R.id.editTextPassword);
        repeatPasswordInput = findViewById(R.id.editTextRepeatPassword);
        birthDateInput = findViewById(R.id.editTextBirthDate);
        genderGroup = findViewById(R.id.genderGroup);
        registerButton = findViewById(R.id.buttonRegister);

        apiService = RetrofitClient.getClient(null).create(ApiService.class);

        registerButton.setOnClickListener(v -> attemptRegister());
    }

    private void attemptRegister() {
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString();
        String repeatPassword = repeatPasswordInput.getText().toString();
        String birthDate = birthDateInput.getText().toString().trim();

        int selectedGenderId = genderGroup.getCheckedRadioButtonId();
        String gender = selectedGenderId == R.id.radioMale ? "male" : "female";

        if (name.length() < 4 || !name.matches("^[\u0400-\u04FF]+$")) {
            Toast.makeText(this, "Името трябва да е поне 4 символа на кирилица", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(repeatPassword)) {
            Toast.makeText(this, "Паролите не съвпадат", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> body = new HashMap<>();
        body.put("Email", email);
        body.put("Firstname", name);
        body.put("BirthDate", birthDate);
        body.put("Gender", gender);
        body.put("Password", password);


        ClientHandler clientHandler = new ClientHandler();
        apiService.register(body).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Регистрация успешна", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                } else {
                    clientHandler.errorRequest(RegisterActivity.this, response);

                    Toast.makeText(RegisterActivity.this, "Регистрацията се провали", Toast.LENGTH_SHORT).show();
                }
                //return false;
            }


            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Грешка при регистрация", Toast.LENGTH_SHORT).show();
            }
        });
    }
}