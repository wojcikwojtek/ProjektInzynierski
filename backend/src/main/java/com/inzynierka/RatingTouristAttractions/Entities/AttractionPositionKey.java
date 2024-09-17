package com.inzynierka.RatingTouristAttractions.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class AttractionPositionKey implements Serializable {

    @Column(name = "attraction_id", nullable = false)
    private long attraction_id;

    @Column(name = "list_id", nullable = false)
    private long list_id;

    public AttractionPositionKey() {}

    public AttractionPositionKey(long attraction_id, long list_id) {
        this.attraction_id = attraction_id;
        this.list_id = list_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AttractionPositionKey that = (AttractionPositionKey) o;

        if (attraction_id != that.attraction_id) return false;
        return list_id == that.list_id;
    }

    @Override
    public int hashCode() {
        int result = (int) (attraction_id ^ (attraction_id >>> 32));
        result = 31 * result + (int) (list_id ^ (list_id >>> 32));
        return result;
    }
}
