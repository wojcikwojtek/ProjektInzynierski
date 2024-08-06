package com.inzynierka.RatingTouristAttractions.Requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    private String login;
    private String password;
    private String email;

    public RegisterRequest(String login, String password, String email) {
        this.login = login;
        this.password = password;
        this.email = email;
    }
}
