package com.inzynierka.RatingTouristAttractions.Requests;

import com.inzynierka.RatingTouristAttractions.Entities.Attraction;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EditListRequest {
    private long listId;
    private List<Attraction> attractions;

    public EditListRequest(long listId, List<Attraction> attractions) {
        this.listId = listId;
        this.attractions = attractions;
    }
}
