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

    @Column(name = "description", columnDefinition="TEXT")
    private String description;

    @Column(name = "size")
    private int size;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "list")
    private List<Like> likes;

    @JsonIgnore
    @ManyToMany(mappedBy = "attractionLists")
    private List<Attraction> attractions;

    public AttractionList() {}

    public AttractionList(String name, String publicationDate, String description, User user, List<Attraction> attractions) {
        this.name = name;
        this.publicationDate = publicationDate;
        this.description = description;
        this.user = user;
        this.attractions = attractions;
        this.size = attractions.size();
    }
}
