package com.inzynierka.RatingTouristAttractions.Requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttractionRequest {
    private String name;
    private String country;
    private String city;
    private String location;
    private String description;

    public AttractionRequest(String name, String country, String city, String location, String description) {
        this.name = name;
        this.country = country;
        this.city = city;
        this.location = location;
        this.description = description;
    }
}
