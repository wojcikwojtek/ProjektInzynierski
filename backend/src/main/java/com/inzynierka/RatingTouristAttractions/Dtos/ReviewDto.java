package com.inzynierka.RatingTouristAttractions.Dtos;

import com.inzynierka.RatingTouristAttractions.Entities.Review;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDto {
    private Review review;
    private long attractionId;
    private String imageUrl;

    public ReviewDto(Review review, long attractionId, String imageUrl) {
        this.review = review;
        this.attractionId = attractionId;
        this.imageUrl = imageUrl;
    }
}
