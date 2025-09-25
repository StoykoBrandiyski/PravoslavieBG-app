package com.example.pravoslaviebg.models.callbacks;

import com.example.pravoslaviebg.models.User;

public interface UserDetailsCallback {
    void onSuccess(User user);
    void onAuthError();
    void onError(String message);
}
