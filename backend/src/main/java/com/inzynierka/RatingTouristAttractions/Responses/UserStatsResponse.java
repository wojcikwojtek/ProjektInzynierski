package com.inzynierka.RatingTouristAttractions.Responses;

import com.inzynierka.RatingTouristAttractions.Dtos.ReviewWithImageDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserStatsResponse {
    private int reviewCount;
    private int listCount;
    private int followingCount;
    private int followersCount;
    private List<ReviewWithImageDto> recentlyReviewed;

    public UserStatsResponse(int reviewCount, int listCount, int followingCount, int followersCount, List<ReviewWithImageDto> recentlyReviewed) {
        this.reviewCount = reviewCount;
        this.listCount = listCount;
        this.followingCount = followingCount;
        this.followersCount = followersCount;
        this.recentlyReviewed = recentlyReviewed;
    }
}
