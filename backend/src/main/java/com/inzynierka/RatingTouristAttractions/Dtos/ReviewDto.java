package com.inzynierka.RatingTouristAttractions.Dtos;

import com.inzynierka.RatingTouristAttractions.Entities.Review;
import com.inzynierka.RatingTouristAttractions.Entities.User;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

@Getter
@Setter
public class ReviewDto {
    private long review_id;
    private double rating;
    private String contents;
    private String publicationDate;
    private User user;

    public ReviewDto(long review_id, double rating, String contents, String publicationDate, User user) {
        this.review_id = review_id;
        this.rating = rating;
        this.contents = contents;
        this.publicationDate = publicationDate;
        this.user = user;
    }

    public ReviewDto(Review review) {
        this.review_id = review.getReview_id();
        this.rating = review.getRating();
        this.contents = review.getContents();
        this.publicationDate = review.getPublicationDate()
                .format(
                        DateTimeFormatter
                                .ofLocalizedDateTime(FormatStyle.SHORT)
                                .withLocale(
                                        new Locale("pl", "PL")
                                )
                );
        this.user = review.getUser();
    }
}
