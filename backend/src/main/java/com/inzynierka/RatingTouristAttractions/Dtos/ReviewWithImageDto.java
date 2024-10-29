package com.inzynierka.RatingTouristAttractions.Dtos;

import com.inzynierka.RatingTouristAttractions.Entities.Review;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewWithImageDto {
    private Review review;
    private long attractionId;
    private String attractionName;

    public ReviewWithImageDto(Review review, long attractionId, String attractionName) {
        this.review = review;
        this.attractionId = attractionId;
        this.attractionName = attractionName;
    }
}
