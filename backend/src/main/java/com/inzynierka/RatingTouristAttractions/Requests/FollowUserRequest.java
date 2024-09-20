package com.inzynierka.RatingTouristAttractions.Requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowUserRequest {
    private long userId;
    private long followedUserId;

    public FollowUserRequest(long userId, long followedUserId) {
        this.userId = userId;
        this.followedUserId = followedUserId;
    }
}
