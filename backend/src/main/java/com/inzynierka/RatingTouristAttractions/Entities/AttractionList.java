package com.inzynierka.RatingTouristAttractions.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "lists")
public class AttractionList {

    @Id
    @Column(name = "list_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long list_id;

    @Column(name = "name")
    private String name;

    @Column(name = "publication_date")
    private String publicationDate;

    @Column(name = "size")
    private int size;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @ManyToMany(mappedBy = "attractionLists")
    private List<Attraction> attractions;

    public AttractionList() {}

    public AttractionList(String name, String publicationDate, User user, List<Attraction> attractions) {
        this.name = name;
        this.publicationDate = publicationDate;
        this.user = user;
        this.attractions = attractions;
        this.size = attractions.size();
    }
}
