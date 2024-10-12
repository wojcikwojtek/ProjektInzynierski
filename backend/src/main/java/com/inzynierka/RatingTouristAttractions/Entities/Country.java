package com.inzynierka.RatingTouristAttractions.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "countries")
public class Country {

    @Id
    @Column(name = "country_id", nullable = false)
    private String country_id;

    @Column(name = "name")
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "country")
    private List<UserCountry> user_country;

    public Country() {}

    public Country(String countryId, String name) {
        this.country_id = countryId;
        this.name = name;
    }
}
