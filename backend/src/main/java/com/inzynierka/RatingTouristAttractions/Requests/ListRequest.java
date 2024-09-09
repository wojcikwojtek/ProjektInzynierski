package com.inzynierka.RatingTouristAttractions.Requests;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListRequest {
    private String name;
    private String description;
    private long userId;
    private List<Long> attractionIds;

    public ListRequest(String name, String description, long userId, List<Long> attractionIds) {
        this.name = name;
        this.description = description;
        this.userId = userId;
        this.attractionIds = attractionIds;
    }
}
