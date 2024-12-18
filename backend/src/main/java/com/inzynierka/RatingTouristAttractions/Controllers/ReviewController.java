package com.inzynierka.RatingTouristAttractions.Controllers;

import com.inzynierka.RatingTouristAttractions.Dtos.ReviewDto;
import com.inzynierka.RatingTouristAttractions.Entities.*;
import com.inzynierka.RatingTouristAttractions.Repositories.*;
import com.inzynierka.RatingTouristAttractions.Requests.ReviewRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final AttractionRepository attractionRepository;
    private final CountryRepository countryRepository;
    private final UserCountryRepository userCountryRepository;

    public ReviewController(ReviewRepository reviewRepository, UserRepository userRepository, AttractionRepository attractionRepository, CountryRepository countryRepository, UserCountryRepository userCountryRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.attractionRepository = attractionRepository;
        this.countryRepository = countryRepository;
        this.userCountryRepository = userCountryRepository;
    }

    @GetMapping
    List<Review> getAllReviews() { return reviewRepository.findAll(); }

    @GetMapping("/{id}")
    ReviewDto getReviewById(@PathVariable long id) {
        Review review = reviewRepository.findById(id).orElse(null);
        if(review == null) return null;
        return new ReviewDto(review);
    }

    @GetMapping("/{id}/likecount")
    int getReviewLikeCount(@PathVariable long id) {
        Review review = reviewRepository.findById(id).orElse(null);
        if (review == null) return -1;
        return review.getLikes().size();
    }

    @GetMapping("/{id}/comments")
    List<Comment> getReviewComments(@PathVariable long id) {
        Review review = reviewRepository.findById(id).orElse(null);
        if (review == null) return null;
        return review.getComments()
                .stream()
                .filter(comment -> !comment.isBlocked())
                .toList();

    }

    @GetMapping("/{id}/commentcount")
    int getReviewCommentCount(@PathVariable long id) {
        Review review = reviewRepository.findById(id).orElse(null);
        if (review == null) return -1;
        return review.getComments().size();
    }

    @PostMapping("/add")
    ResponseEntity<?> addReview(@RequestBody ReviewRequest reviewRequest) {
        User user = userRepository.findById(reviewRequest.getUserId()).orElse(null);
        if(user == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        Attraction attraction = attractionRepository.findById(reviewRequest.getAttractionId()).orElse(null);
        if(attraction == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Attraction not found");
        String dateTime = LocalDateTime
                .now().format(
                DateTimeFormatter
                        .ofLocalizedDateTime(FormatStyle.SHORT)
                        .withLocale(
                                new Locale("pl", "PL")
                        )
        );
        Review review = new Review(
                reviewRequest.getRating(),
                reviewRequest.getContents(),
                user,
                attraction
        );
        reviewRepository.save(review);
        boolean didUserVisit = false;
        for(UserCountry userCountry : user.getVisitedCountries()) {
            if(attraction.getCountry().equals(userCountry.getCountry().getName())) didUserVisit = true;
        }
        if(!didUserVisit) {
            Country country = countryRepository.findByName(attraction.getCountry());
            UserCountry userCountry = new UserCountry(user, country);
            userCountryRepository.save(userCountry);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(review);
    }

    @PostMapping("/report")
    ResponseEntity<?> reportReview(@RequestParam long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElse(null);
        if(review == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Review not found");
        review.setReported(true);
        reviewRepository.save(review);
        return ResponseEntity.status(HttpStatus.CREATED).body(review);
    }

    @DeleteMapping("/{id}")
    void deleteReviewById(@PathVariable long id) { reviewRepository.deleteById(id); }
}
