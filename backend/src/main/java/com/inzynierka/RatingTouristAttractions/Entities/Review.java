package com.inzynierka.RatingTouristAttractions.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @Column(name = "review_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long review_id;

    @Column(name = "rating")
    private double rating;

    @Column(name = "contents", columnDefinition="TEXT")
    private String contents;

    @Column(name = "publication_date")
    private LocalDateTime publicationDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "attraction_id")
    private Attraction attraction;

    @JsonIgnore
    @OneToMany(mappedBy = "review")
    private List<Like> likes;

    @JsonIgnore
    @OneToMany(mappedBy = "review")
    private List<Comment> comments;

    public Review() {}

    public Review(double rating, String contents, User user, Attraction attraction) {
        this.rating = rating;
        this.contents = contents;
        this.publicationDate = LocalDateTime.now();
        this.user = user;
        this.attraction = attraction;
    }
}
