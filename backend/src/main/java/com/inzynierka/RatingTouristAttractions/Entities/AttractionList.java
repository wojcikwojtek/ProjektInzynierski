package com.inzynierka.RatingTouristAttractions.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private LocalDateTime publicationDate;

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
    @OneToMany(mappedBy = "list")
    private List<AttractionPosition> attractions = new ArrayList<>();

    public AttractionList() {}

    public AttractionList(String name, String description, User user) {
        this.name = name;
        this.publicationDate = LocalDateTime.now();
        this.description = description;
        this.user = user;
        this.size = attractions.size();
    }
}
