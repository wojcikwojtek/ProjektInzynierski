package com.inzynierka.RatingTouristAttractions.Requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {

    private String login;
    private String password;
    private String email;

    public UserRequest(String login, String password, String email) {
        this.login = login;
        this.password = password;
        this.email = email;
    }
}
