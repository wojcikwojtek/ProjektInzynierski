package com.inzynierka.RatingTouristAttractions.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class UserCountry {

    @EmbeddedId
    UserCountryKey id;

    @ManyToOne
    @MapsId("user_id")
    @JoinColumn(name = "user_id")
    User user;

    @JsonIgnore
    @ManyToOne
    @MapsId("country_id")
    @JoinColumn(name = "country_id")
    Country country;

    public UserCountry() {}

    public UserCountry(UserCountryKey id, User user, Country country) {
        this.id = id;
        this.user = user;
        this.country = country;
    }
}
