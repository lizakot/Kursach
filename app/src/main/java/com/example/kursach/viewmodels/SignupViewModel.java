package com.example.kursach.viewmodels;

import android.util.Patterns;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SignupViewModel extends ViewModel {

    private MutableLiveData<Boolean> isNameValid = new MutableLiveData<>();
    private MutableLiveData<Boolean> isEmailValid = new MutableLiveData<>();
    private MutableLiveData<Boolean> isUsernameValid = new MutableLiveData<>();
    private MutableLiveData<Boolean> isPasswordValid = new MutableLiveData<>();

    public MutableLiveData<Boolean> getIsNameValid() {
        return isNameValid;
    }

    public MutableLiveData<Boolean> getIsEmailValid() {
        return isEmailValid;
    }

    public MutableLiveData<Boolean> getIsUsernameValid() {
        return isUsernameValid;
    }

    public MutableLiveData<Boolean> getIsPasswordValid() {
        return isPasswordValid;
    }

    public void validateName(String name) {
        isNameValid.setValue(!name.isEmpty());
    }

    public void validateEmail(String email) {
        // Проверка на наличие символа "@" в адресе электронной почты
        isEmailValid.setValue(Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    public void validateUsername(String username) {
        // Проверка, что введенное имя пользователя не содержит пробелов
        isUsernameValid.setValue(!username.isEmpty() && !username.contains(" "));
    }

    public void validatePassword(String password) {
        // Регулярное выражение для проверки наличия хотя бы одной заглавной буквы и одной цифры
        String passwordRegex = "^(?=.*[A-Z])(?=.*\\d).+$";
        isPasswordValid.setValue(password.length() >= 8 && password.matches(passwordRegex));
    }
}
