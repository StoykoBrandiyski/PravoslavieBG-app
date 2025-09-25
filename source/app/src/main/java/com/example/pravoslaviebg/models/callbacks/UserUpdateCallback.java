package com.example.pravoslaviebg.models.callbacks;

import com.example.pravoslaviebg.models.User;

public interface UserUpdateCallback {
    void onResult(boolean success);
    void onAuthError();
    void onError(String message);
}
