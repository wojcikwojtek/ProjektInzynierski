package com.inzynierka.RatingTouristAttractions.Dtos;

import com.inzynierka.RatingTouristAttractions.Entities.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    User user;
    boolean isFollowing;

    public UserDto(User user, boolean isFollowing) {
        this.user = user;
        this.isFollowing = isFollowing;
    }
}
