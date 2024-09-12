package com.inzynierka.RatingTouristAttractions.Requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeRequest {
    private long userId;
    private long reviewId;
    private long listId;

    public LikeRequest(long userId, long reviewId, long listId) {
        this.userId = userId;
        this.reviewId = reviewId;
        this.listId = listId;
    }
}
