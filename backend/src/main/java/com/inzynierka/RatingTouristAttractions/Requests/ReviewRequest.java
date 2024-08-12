package com.inzynierka.RatingTouristAttractions.Requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequest {
    private long userId;
    private long attractionId;
    private int rating;
    private String contents;

    public ReviewRequest(long userId, long attractionId, int rating, String contents) {
        this.userId = userId;
        this.attractionId = attractionId;
        this.rating = rating;
        this.contents = contents;
    }
}
