package com.inzynierka.RatingTouristAttractions.Requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {
    private long userId;
    private String login;
    private String password;
    private String email;

    public UserUpdateRequest(long userId, String login, String password, String email) {
        this.userId = userId;
        this.login = login;
        this.password = password;
        this.email = email;
    }
}
