package com.inzynierka.RatingTouristAttractions.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @Column(name = "comment_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long comment_id;

    @Column(name = "contents", columnDefinition="TEXT")
    private String contents;

    @Column(name = "publication_date")
    private String publicationDate;

    @Column(name = "is_reported")
    private boolean isReported;

    @Column(name = "is_blocked")
    private boolean isBlocked;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

    public Comment() {}

    public Comment(String contents, String publicationDate, User user, Review review) {
        this.contents = contents;
        this.publicationDate = publicationDate;
        this.isReported = false;
        this.isBlocked = false;
        this.user = user;
        this.review = review;
    }
}
