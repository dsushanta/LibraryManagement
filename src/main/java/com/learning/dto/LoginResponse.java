package com.learning.dto;

public class LoginResponse {

    private boolean authenticated;

    public LoginResponse() {
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }
}
