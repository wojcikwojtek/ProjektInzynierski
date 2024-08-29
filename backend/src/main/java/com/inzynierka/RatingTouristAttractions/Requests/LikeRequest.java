package com.inzynierka.RatingTouristAttractions.Requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeRequest {
    private long userId;
    private long reviewId;

    public LikeRequest(long userId, long reviewId) {
        this.userId = userId;
        this.reviewId = reviewId;
    }
}
