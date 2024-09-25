package com.inzynierka.RatingTouristAttractions.Dtos;

import com.inzynierka.RatingTouristAttractions.Entities.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttractionListDto {
    private long list_id;
    private String name;
    private String description;
    private String publicationDate;
    private int size;
    private User user;

    public AttractionListDto(long list_id, String name, String description, String publicationDate, int size, User user) {
        this.list_id = list_id;
        this.name = name;
        this.description = description;
        this.publicationDate = publicationDate;
        this.size = size;
        this.user = user;
    }
}
