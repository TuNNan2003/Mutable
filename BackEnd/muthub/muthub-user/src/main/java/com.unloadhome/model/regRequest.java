package com.unloadhome.model;

import lombok.Data;

@Data
public class regRequest {
    private String name;
    private String password;
    private String email;

    public regRequest(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public regRequest() {
    }
}
