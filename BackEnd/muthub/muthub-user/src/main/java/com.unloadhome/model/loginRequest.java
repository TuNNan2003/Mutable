package com.unloadhome.model;

public class loginRequest {
    private String email;
    private String password;

    public loginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public loginRequest(){}

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
