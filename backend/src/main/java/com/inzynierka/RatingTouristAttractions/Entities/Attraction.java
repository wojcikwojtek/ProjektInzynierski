package com.inzynierka.RatingTouristAttractions.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "attractions")
public class Attraction {
    @Id
    @Column(name = "attraction_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long attraction_id;

    @Column(name = "name")
    private String name;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "location")
    private String location;

    @Column(name = "description")
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    public Attraction() {}

    public Attraction(String name, String country, String city, String location, String description, String imageUrl) {
        this.name = name;
        this.country = country;
        this.city = city;
        this.location = location;
        this.description = description;
        this.imageUrl = imageUrl;
    }
}
