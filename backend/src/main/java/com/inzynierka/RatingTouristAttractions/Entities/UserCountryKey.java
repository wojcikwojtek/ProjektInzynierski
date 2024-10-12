package com.inzynierka.RatingTouristAttractions.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class UserCountryKey implements Serializable {
    @Column(name = "user_id", nullable = false)
    private long user_id;

    @Column(name = "country_id", nullable = false)
    private String country_id;

    public UserCountryKey() {}

    public UserCountryKey(long user_id, String country_id) {
        this.user_id = user_id;
        this.country_id = country_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserCountryKey that = (UserCountryKey) o;

        if (user_id != that.user_id) return false;
        return country_id.equals(that.country_id);
    }

    @Override
    public int hashCode() {
        int result = (int) (user_id ^ (user_id >>> 32));
        result = 31 * result;
        return result;
    }
}
