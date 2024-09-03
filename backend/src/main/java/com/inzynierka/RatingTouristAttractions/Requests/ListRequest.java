package com.inzynierka.RatingTouristAttractions.Requests;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListRequest {
    private String name;
    private long userId;
    private List<Long> attractionIds;

    public ListRequest(String name, long userId, List<Long> attractionIds) {
        this.name = name;
        this.userId = userId;
        this.attractionIds = attractionIds;
    }
}
