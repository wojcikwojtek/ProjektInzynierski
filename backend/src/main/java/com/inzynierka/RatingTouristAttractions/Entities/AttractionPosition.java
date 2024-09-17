package com.inzynierka.RatingTouristAttractions.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class AttractionPosition {

    @EmbeddedId
    AttractionPositionKey id;

    @ManyToOne
    @MapsId("attraction_id")
    @JoinColumn(name = "attraction_id")
    Attraction attraction;

    @JsonIgnore
    @ManyToOne
    @MapsId("list_id")
    @JoinColumn(name = "list_id")
    AttractionList list;

    int position;

    public AttractionPosition() {}

    public AttractionPosition(Attraction attraction, AttractionList list, int position) {
        this.id = new AttractionPositionKey(attraction.getAttraction_id(), list.getList_id());
        this.attraction = attraction;
        this.list = list;
        this.position = position;
    }
}
