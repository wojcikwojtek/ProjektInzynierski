package com.inzynierka.RatingTouristAttractions.Controllers;

import com.inzynierka.RatingTouristAttractions.Entities.AttractionList;
import com.inzynierka.RatingTouristAttractions.Entities.Like;
import com.inzynierka.RatingTouristAttractions.Entities.Review;
import com.inzynierka.RatingTouristAttractions.Entities.User;
import com.inzynierka.RatingTouristAttractions.Repositories.AttractionListRepository;
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
    private final AttractionListRepository attractionListRepository;

    public LikeController(LikeRepository likeRepository, UserRepository userRepository, ReviewRepository reviewRepository, AttractionListRepository attractionListRepository) {
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
        this.attractionListRepository = attractionListRepository;
    }

    @GetMapping
    List<Like> getAllLikes() { return likeRepository.findAll(); }

    @GetMapping("/{id}")
    Like getLikeById(@PathVariable long id) {
        return likeRepository.findById(id).orElse(null);
    }

    @GetMapping("/{userId}/reviewcount")
    int getReviewCount(@PathVariable long userId) {
        return likeRepository.countUserLikesByReview(userId);
    }

    @GetMapping("/{userId}/listcount")
    int getListCount(@PathVariable long userId) {
        return likeRepository.countUserLikesByList(userId);
    }

    @GetMapping("/{userId}/likedreview/{reviewId}")
    boolean didUserLikedReview(@PathVariable long userId, @PathVariable long reviewId) {
        User user = userRepository.findById(userId).orElse(null);
        Review review = reviewRepository.findById(reviewId).orElse(null);
        if(user == null || review == null) return false;
        if(likeRepository.findByReviewAndUser(review, user)!=null) return true;
        return false;
    }

    @GetMapping("{userId}/likedlist/{listId}")
    boolean didUserLikedList(@PathVariable long userId, @PathVariable long listId) {
        User user = userRepository.findById(userId).orElse(null);
        AttractionList list = attractionListRepository.findById(listId).orElse(null);
        if(user == null || list == null) return false;
        if(likeRepository.findByListAndUser(list, user)!=null) return true;
        return false;
    }

    @PostMapping("/review")
    ResponseEntity<?> likeReview(@RequestBody LikeRequest likeRequest) {
        User user = userRepository.findById(likeRequest.getUserId()).orElse(null);
        if (user == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        Review review = reviewRepository.findById(likeRequest.getReviewId()).orElse(null);
        if (review == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Review not found");
        Like like = new Like(user, review, null);
        likeRepository.save(like);
        return ResponseEntity.status(HttpStatus.CREATED).body(like);
    }

    @PostMapping("/list")
    ResponseEntity<?> likeList(@RequestBody LikeRequest likeRequest) {
        User user = userRepository.findById(likeRequest.getUserId()).orElse(null);
        if (user == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        AttractionList attractionList = attractionListRepository.findById(likeRequest.getListId()).orElse(null);
        if (attractionList == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("List not found");
        Like like = new Like(user, null, attractionList);
        likeRepository.save(like);
        return ResponseEntity.status(HttpStatus.CREATED).body(like);
    }

    @DeleteMapping("/{userId}/review/{reviewId}")
    void deleteLike(@PathVariable long userId, @PathVariable long reviewId) {
        User user = userRepository.findById(userId).orElse(null);
        Review review = reviewRepository.findById(reviewId).orElse(null);
        if(user == null || review == null) return;
        Like like = likeRepository.findByReviewAndUser(review, user);
        likeRepository.delete(like);
    }

    @DeleteMapping("/{userId}/list/{listId}")
    void deleteList(@PathVariable long userId, @PathVariable long listId) {
        User user = userRepository.findById(userId).orElse(null);
        AttractionList list = attractionListRepository.findById(listId).orElse(null);
        if(user == null || list == null) return;
        Like like = likeRepository.findByListAndUser(list, user);
        likeRepository.delete(like);
    }
}
