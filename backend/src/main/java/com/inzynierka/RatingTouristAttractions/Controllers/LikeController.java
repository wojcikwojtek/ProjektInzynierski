package com.inzynierka.RatingTouristAttractions.Controllers;

import com.inzynierka.RatingTouristAttractions.Entities.Like;
import com.inzynierka.RatingTouristAttractions.Entities.Review;
import com.inzynierka.RatingTouristAttractions.Entities.User;
import com.inzynierka.RatingTouristAttractions.Repositories.LikeRepository;
import com.inzynierka.RatingTouristAttractions.Repositories.ReviewRepository;
import com.inzynierka.RatingTouristAttractions.Repositories.UserRepository;
import com.inzynierka.RatingTouristAttractions.Requests.LikeRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/likes")
public class LikeController {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    public LikeController(LikeRepository likeRepository, UserRepository userRepository, ReviewRepository reviewRepository) {
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
    }

    @GetMapping
    List<Like> getAllLikes() { return likeRepository.findAll(); }

    @GetMapping("/{id}")
    Like getLikeById(@PathVariable long id) {
        return likeRepository.findById(id).orElse(null);
    }

    @GetMapping("{userId}/liked/{reviewId}")
    boolean didUserLikedReview(@PathVariable long userId, @PathVariable long reviewId) {
        User user = userRepository.findById(userId).orElse(null);
        Review review = reviewRepository.findById(reviewId).orElse(null);
        if(user == null || review == null) return false;
        for(Like like : user.getLikes()) {
            if(like.getReview().equals(review)) {
                return true;
            }
        }
        return false;
    }

    @PostMapping("/review")
    ResponseEntity<?> likeReview(@RequestBody LikeRequest likeRequest) {
        User user = userRepository.findById(likeRequest.getUserId()).orElse(null);
        if (user == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        Review review = reviewRepository.findById(likeRequest.getReviewId()).orElse(null);
        if (review == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Review not found");
        Like like = new Like(user, review);
        likeRepository.save(like);
        return ResponseEntity.status(HttpStatus.CREATED).body(like);
    }

    @DeleteMapping("/{userId}/{reviewId}")
    void deleteLike(@PathVariable long userId, @PathVariable long reviewId) {
        User user = userRepository.findById(userId).orElse(null);
        Review review = reviewRepository.findById(reviewId).orElse(null);
        if(user == null || review == null) return;
        Like like = likeRepository.findByUserAndReview(user, review);
        likeRepository.delete(like);
    }
}
