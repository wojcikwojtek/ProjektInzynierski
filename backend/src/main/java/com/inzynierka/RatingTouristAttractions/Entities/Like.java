package com.inzynierka.RatingTouristAttractions.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "likes")
public class Like {

    @Id
    @Column(name = "like_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long like_id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "list_id")
    private AttractionList list;

    public Like() {}

    public Like(User user, Review review, AttractionList list) {
        this.user = user;
        this.review = review;
        this.list = list;
    }
}
