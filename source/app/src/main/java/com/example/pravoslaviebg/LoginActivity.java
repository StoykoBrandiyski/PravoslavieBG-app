package com.example.pravoslaviebg;

import androidx.appcompat.app.AppCompatActivity;
import com.example.pravoslaviebg.models.AuthResponse;
import com.example.pravoslaviebg.network.ApiService;
import com.example.pravoslaviebg.network.ClientHandler;
import com.example.pravoslaviebg.network.RetrofitClient;
import com.example.pravoslaviebg.utils.TokenManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText emailInput, passwordInput;
    Button loginButton;
    TextView registerLink;
    ProgressBar progressBar;
    ApiService apiService;

    private int loginAttempts = 0;
    private static final int MAX_ATTEMPTS = 3;
    private static final long LOCKOUT_DURATION_MS = 60 * 1000; // 1 minute

    private long lockoutStartTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.editEmail);
        passwordInput = findViewById(R.id.editPassword);
        loginButton = findViewById(R.id.btnLogin);
        registerLink = findViewById(R.id.linkRegister);
        progressBar = findViewById(R.id.progressBar);

        apiService = RetrofitClient.getClient(null).create(ApiService.class);

        loginButton.setOnClickListener(v -> attemptLogin());

        registerLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        String token = TokenManager.getToken(LoginActivity.this);

        if(token != null ){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    private void attemptLogin() {
        long currentTime = System.currentTimeMillis();

        if (loginAttempts >= MAX_ATTEMPTS) {
            if (currentTime - lockoutStartTime < LOCKOUT_DURATION_MS) {
                long secondsLeft = (LOCKOUT_DURATION_MS - (currentTime - lockoutStartTime)) / 1000;
                Toast.makeText(this, "Прекалено много опити. Опитайте след " + secondsLeft + " сек.", Toast.LENGTH_SHORT).show();
                return;
            } else {
                loginAttempts = 0;
            }
        }
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Моля въведете имейл и парола", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        Map<String, String> body = new HashMap<>();
        body.put("Email", email);
        body.put("Password", password);

        ClientHandler clientHandler = new ClientHandler();
        apiService.login(body).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    String token = response.body().toString();
                    TokenManager.saveToken(LoginActivity.this, token);

                    // Redirect to Main/Dashboard
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    loginAttempts++;
                    if (loginAttempts >= MAX_ATTEMPTS) {
                        lockoutStartTime = System.currentTimeMillis();
                        Toast.makeText(LoginActivity.this, "Прекалено много неуспешни опити. Опитайте отново след 1 минута.", Toast.LENGTH_LONG).show();

                    }
                    clientHandler.errorRequest(LoginActivity.this, response);
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, "Грешка при връзка със сървъра", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
