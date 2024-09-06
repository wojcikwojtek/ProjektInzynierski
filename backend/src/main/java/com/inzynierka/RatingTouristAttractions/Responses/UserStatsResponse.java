package com.inzynierka.RatingTouristAttractions.Responses;

import com.inzynierka.RatingTouristAttractions.Entities.Attraction;
import com.inzynierka.RatingTouristAttractions.Helpers.ReviewWithImage;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserStatsResponse {
    private int reviewCount;
    private int listCount;
    private List<ReviewWithImage> recentlyReviewed;

    public UserStatsResponse(int reviewCount, int listCount, List<ReviewWithImage> recentlyReviewed) {
        this.reviewCount = reviewCount;
        this.listCount = listCount;
        this.recentlyReviewed = recentlyReviewed;
    }
}
