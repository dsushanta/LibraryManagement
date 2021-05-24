package com.bravo.johny.dto;

import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;

public class LoginResponse extends RepresentationModel<LoginResponse> implements Serializable {

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
