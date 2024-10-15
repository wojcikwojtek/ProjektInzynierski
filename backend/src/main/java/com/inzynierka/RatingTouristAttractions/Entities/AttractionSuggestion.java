package com.inzynierka.RatingTouristAttractions.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "attraction_suggestions")
public class AttractionSuggestion {

    @JsonIgnore
    @Id
    @Column(name = "suggestion_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long suggestion_id;

    @Column(name = "name")
    private String name;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "location")
    private String location;

    @Column(name = "description", columnDefinition="TEXT")
    private String description;

    public AttractionSuggestion() {}

    public AttractionSuggestion(String name, String country, String city, String location, String description) {
        this.name = name;
        this.country = country;
        this.city = city;
        this.location = location;
        this.description = description;
    }
}
