package com.inzynierka.RatingTouristAttractions.Requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {
    private String contents;
    private long userId;
    private long reviewId;

    public CommentRequest(String contents, long userId, long reviewId) {
        this.contents = contents;
        this.userId = userId;
        this.reviewId = reviewId;
    }
}
