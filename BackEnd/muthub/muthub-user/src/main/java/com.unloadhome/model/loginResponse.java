package com.unloadhome.model;

public class loginResponse {
    private boolean success;
    private String token;

    public loginResponse(boolean success, String token) {
        this.success = success;
        this.token = token;
    }
}
