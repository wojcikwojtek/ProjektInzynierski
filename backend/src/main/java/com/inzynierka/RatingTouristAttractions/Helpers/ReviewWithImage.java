package com.inzynierka.RatingTouristAttractions.Helpers;

import com.inzynierka.RatingTouristAttractions.Entities.Review;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewWithImage {
    private Review review;
    private long attractionId;
    private String imageUrl;

    public ReviewWithImage(Review review, long attractionId, String imageUrl) {
        this.review = review;
        this.attractionId = attractionId;
        this.imageUrl = imageUrl;
    }
}
