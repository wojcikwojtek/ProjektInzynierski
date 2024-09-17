package com.inzynierka.RatingTouristAttractions.Requests;

import com.inzynierka.RatingTouristAttractions.Entities.Attraction;
import com.inzynierka.RatingTouristAttractions.Entities.AttractionPosition;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EditListRequest {
    private long listId;
    private List<AttractionPosition> attractions;

    public EditListRequest(long listId, List<AttractionPosition> attractions) {
        this.listId = listId;
        this.attractions = attractions;
    }
}
