package com.inzynierka.RatingTouristAttractions.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "user_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long user_id;

    @Column(name = "login", nullable = false)
    private String login;

    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;

    @JsonIgnore
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "is_admin", nullable = false)
    private boolean isAdmin;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Review> reviews;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Like> likes;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<AttractionList> lists;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<UserCountry> visitedCountries;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "user_following",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "followed_user_id")
    )
    private List<User> followedUsers;

    public User() {}

    public User(String login, String password, String email) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.isAdmin = false;
    }
}
