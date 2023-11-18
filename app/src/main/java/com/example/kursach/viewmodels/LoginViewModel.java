package com.example.kursach.viewmodels;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<String> usernameError = new MutableLiveData<>();
    private MutableLiveData<String> passwordError = new MutableLiveData<>();

    public MutableLiveData<String> getUsernameError() {
        return usernameError;
    }

    public MutableLiveData<String> getPasswordError() {
        return passwordError;
    }

    public void validateUsername(String username) {
        if (username.isEmpty()) {
            usernameError.setValue("Username cannot be empty");
        } else {
            usernameError.setValue(null);
        }
    }

    public void validatePassword(String password) {
        if (password.isEmpty()) {
            passwordError.setValue("Password cannot be empty");
        } else {
            passwordError.setValue(null);
        }
    }
}

