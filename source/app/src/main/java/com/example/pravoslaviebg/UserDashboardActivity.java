package com.example.pravoslaviebg;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.MutableLiveData;

import com.example.pravoslaviebg.models.User;
import com.example.pravoslaviebg.models.callbacks.UserDetailsCallback;
import com.example.pravoslaviebg.models.callbacks.UserUpdateCallback;
import com.example.pravoslaviebg.repositories.SettingsRepository;
import com.example.pravoslaviebg.repositories.UserRepository;
import com.example.pravoslaviebg.utils.TokenManager;

public class UserDashboardActivity extends AppCompatActivity {
    private SettingsRepository settingRepository;
    private EditText name, email, birthDate, password;
    private RadioGroup genderGroup;
    private ProgressBar progressBar;
    private Button saveButton;
    private SwitchCompat notificationSwitch;
    private MutableLiveData<User> detailsLiveData = new MutableLiveData<>();
    private final UserRepository userRepository;

    public UserDashboardActivity() {
        this.userRepository = new UserRepository();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.settingRepository =  new SettingsRepository(UserDashboardActivity.this);

        setContentView(R.layout.activity_user_dashboard);
        notificationSwitch = findViewById(R.id.switchDailyNotifications);
        name = findViewById(R.id.editTextName);
        email = findViewById(R.id.editTextEmail);
        birthDate = findViewById(R.id.editTextBirthDate);
        password = findViewById(R.id.editTextPassword);
        genderGroup = findViewById(R.id.genderGroup);
        progressBar = findViewById(R.id.progressBar);

        saveButton = findViewById(R.id.buttonRegister);
        notificationSwitch.setChecked(this.settingRepository.isNotificationEnabled());
        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            settingRepository.setNotificationEnabled(isChecked);
        });

        this.userRepository.getUserDetails(TokenManager.getToken(this), new UserDetailsCallback() {
            @Override
            public void onSuccess(User user) {
                detailsLiveData.setValue(user);
            }

            @Override
            public void onAuthError() {
                Toast.makeText(UserDashboardActivity.this, "Изисква се повторно влизане", Toast.LENGTH_LONG).show();
                TokenManager.clearToken(UserDashboardActivity.this);
                Intent intent = new Intent(UserDashboardActivity.this, LoginActivity.class);
                startActivity(intent);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(UserDashboardActivity.this, "Грешка: " + message, Toast.LENGTH_SHORT).show();
            }
        });

        detailsLiveData.observe(this, user -> {
            if (user != null) {
                name.setText(user.getName());
                email.setText(user.getEmail());
                birthDate.setText(user.getBirthDate());

                if ("male".equalsIgnoreCase(user.getGender())) {
                    genderGroup.check(R.id.radioMale);
                } else {
                    genderGroup.check(R.id.radioFemale);
                }
            } else {
                Toast.makeText(
                        UserDashboardActivity.this,
                        "User not found",
                        Toast.LENGTH_LONG
                ).show();
            }
        });

        saveButton.setOnClickListener(v -> {
            String selectedGender = (genderGroup.getCheckedRadioButtonId() == R.id.radioMale) ? "male" : "female";

            String nameStr = name.getText().toString().trim();
            String emailStr = email.getText().toString().trim();
            String birthDateStr = birthDate.getText().toString().trim();
            String passwordStr = password.getText().toString().trim();

            if (nameStr.isEmpty()) {
                name.setError("Моля, въведете име");
                return;
            }

            if (!emailStr.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                email.setError("Невалиден имейл");
                return;
            }

            User updatedUser = new User();
            updatedUser.setName(name.getText().toString());
            updatedUser.setEmail(email.getText().toString());
            updatedUser.setGender(selectedGender);
            updatedUser.setBirthDate(birthDate.getText().toString());
            if (!password.getText().toString().isEmpty()) {
                updatedUser.setPassword(password.getText().toString());
            }

            progressBar.setVisibility(View.VISIBLE);
            disableFields(true);

            userRepository.updateUser(TokenManager.getToken(this), updatedUser, new UserUpdateCallback() {
                @Override
                public void onResult(boolean success) {
                    Toast.makeText(UserDashboardActivity.this, "Успешно обновяване", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAuthError() {
                    Toast.makeText(UserDashboardActivity.this, "Изисква се повторно влизане", Toast.LENGTH_LONG).show();
                    TokenManager.clearToken(UserDashboardActivity.this);
                    Intent intent = new Intent(UserDashboardActivity.this, LoginActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onError(String message) {
                    Toast.makeText(UserDashboardActivity.this, "Неуспешно обновяване", Toast.LENGTH_SHORT).show();
                }
            });

            progressBar.setVisibility(View.GONE);
            disableFields(false);

        });
    }

    private void disableFields(boolean disable) {
        name.setEnabled(!disable);
        email.setEnabled(!disable);
        birthDate.setEnabled(!disable);
        password.setEnabled(!disable);
        genderGroup.getChildAt(0).setEnabled(!disable);
        genderGroup.getChildAt(1).setEnabled(!disable);
        saveButton.setEnabled(!disable);
    }
}
