package com.unloadhome.model;

public class loginRequest {
    private String name;
    private String password;

    public loginRequest(String name, String password) {
        this.name = name;
        this.password = password;

    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
