package com.example.clientsellingmedicine.models;

public class UserLogin {
    private String phone;
    private String password;

    public UserLogin() {
    }

    public UserLogin(String phone, String password) {
        this.phone = phone;
        this.password = password;
    }
}
