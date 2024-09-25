package com.inzynierka.RatingTouristAttractions.Dtos;

import com.inzynierka.RatingTouristAttractions.Entities.Review;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewWithImageDto {
    private ReviewDto review;
    private long attractionId;
    private String attractionName;
    private String imageUrl;

    public ReviewWithImageDto(ReviewDto review, long attractionId, String attractionName, String imageUrl) {
        this.review = review;
        this.attractionId = attractionId;
        this.attractionName = attractionName;
        this.imageUrl = imageUrl;
    }
}
