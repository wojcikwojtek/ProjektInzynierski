package com.inzynierka.RatingTouristAttractions.Requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddToListRequest {
    private long listId;
    private long attractionId;

    public AddToListRequest(long listId, long attractionId) {
        this.listId = listId;
        this.attractionId = attractionId;
    }
}
