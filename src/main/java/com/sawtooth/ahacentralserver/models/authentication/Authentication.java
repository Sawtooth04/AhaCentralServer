package com.sawtooth.ahacentralserver.models.authentication;

import org.springframework.hateoas.RepresentationModel;

public class Authentication extends RepresentationModel<Authentication> {
    public boolean isAuthenticated;
    public String name;

    public Authentication() {
        isAuthenticated = false;
    }
}
