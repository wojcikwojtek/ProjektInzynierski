package com.inzynierka.RatingTouristAttractions.Responses;

import com.inzynierka.RatingTouristAttractions.Dtos.ReviewDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserStatsResponse {
    private int reviewCount;
    private int listCount;
    private List<ReviewDto> recentlyReviewed;

    public UserStatsResponse(int reviewCount, int listCount, List<ReviewDto> recentlyReviewed) {
        this.reviewCount = reviewCount;
        this.listCount = listCount;
        this.recentlyReviewed = recentlyReviewed;
    }
}
