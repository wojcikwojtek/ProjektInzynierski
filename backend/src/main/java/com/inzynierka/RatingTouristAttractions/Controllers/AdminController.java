package com.inzynierka.RatingTouristAttractions.Controllers;

import com.inzynierka.RatingTouristAttractions.Dtos.ReviewDto;
import com.inzynierka.RatingTouristAttractions.Entities.*;
import com.inzynierka.RatingTouristAttractions.Repositories.*;
import com.inzynierka.RatingTouristAttractions.Requests.AttractionRequest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AttractionSuggestionRepository attractionSuggestionRepository;
    private final UserRepository userRepository;
    private final AttractionRepository attractionRepository;
    private final ReviewRepository reviewRepository;
    private final CommentRepository commentRepository;

    public AdminController(AttractionSuggestionRepository attractionSuggestionRepository, UserRepository userRepository, AttractionRepository attractionRepository, ReviewRepository reviewRepository, CommentRepository commentRepository) {
        this.attractionSuggestionRepository = attractionSuggestionRepository;
        this.userRepository = userRepository;
        this.attractionRepository = attractionRepository;
        this.reviewRepository = reviewRepository;
        this.commentRepository = commentRepository;
    }

    @GetMapping("/{id}/makeadmin")
    void makeAdmin(@PathVariable long id) {
        User user = userRepository.findById(id).orElse(null);
        user.setAdmin(true);
        userRepository.save(user);
    }

    @GetMapping("/suggestions")
    List<AttractionSuggestion> getSuggestions() { return attractionSuggestionRepository.findAll(); }

    @GetMapping("/suggestion/{id}/image")
    ResponseEntity<?> getSuggestionImage(@PathVariable long id) {
        try {
            Path path = Paths.get("src/main/resources/images/attraction/Suggestion" + id + ".jpg");
            if(!Files.exists(path)) {
                path = Paths.get("src/main/resources/images/attraction/ImageNotFound.jpg");
            }
            Resource resource = new UrlResource(path.toUri());
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(resource);
        } catch (MalformedURLException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Image url is invalid");
        }
    }

    @GetMapping("/reported/reviews")
    List<ReviewDto> getReportedReviews() {
        List<Review> reviews = reviewRepository.findByIsReportedTrue();
        List<ReviewDto> reviewDtos = new ArrayList<>();
        for (Review review : reviews) {
            reviewDtos.add(new ReviewDto(review));
        }
        return reviewDtos;
    }

    @GetMapping("/reported/comments")
    List<Comment> getReportedComments() { return commentRepository.findByIsReportedTrue(); }

    @PostMapping("/addsuggestion")
    ResponseEntity<?> addSuggestion(@RequestPart("suggestion") AttractionRequest suggestion, @RequestPart("file") MultipartFile file) {
        AttractionSuggestion attractionSuggestion = new AttractionSuggestion(
                suggestion.getName(),
                suggestion.getCountry(),
                suggestion.getCity(),
                suggestion.getLocation(),
                suggestion.getDescription()
        );
        attractionSuggestionRepository.save(attractionSuggestion);
        try {
            FileController fileController = new FileController();
            fileController.uploadSuggestion(file, attractionSuggestion.getSuggestion_id());
            return ResponseEntity.ok().body(attractionSuggestion);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/suggestion/approve")
    ResponseEntity<?> approveSuggestion(@RequestParam long suggestionId) {
        AttractionSuggestion attractionSuggestion = attractionSuggestionRepository.findById(suggestionId).orElse(null);
        if (attractionSuggestion == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Suggestion not found");
        AttractionController attractionController = new AttractionController(attractionRepository);
        Attraction addedAttraction = attractionController.addAttraction(new AttractionRequest(
                attractionSuggestion.getName(),
                attractionSuggestion.getCountry(),
                attractionSuggestion.getCity(),
                attractionSuggestion.getLocation(),
                attractionSuggestion.getDescription()
        ));
        FileController fileController = new FileController();
        if(fileController.renameAttractionImage(attractionSuggestion.getSuggestion_id(), addedAttraction.getAttraction_id())) {
            attractionSuggestionRepository.delete(attractionSuggestion);
            return ResponseEntity.ok().body(addedAttraction);
        } else {
            attractionRepository.delete(addedAttraction);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Could not rename attraction image");
        }
    }

    @PostMapping("/suggestion/discard")
    ResponseEntity<?> discardSuggestion(@RequestParam long suggestionId) {
        AttractionSuggestion attractionSuggestion = attractionSuggestionRepository.findById(suggestionId).orElse(null);
        if (attractionSuggestion == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Suggestion not found");
        FileController fileController = new FileController();
        if(fileController.deleteSuggestion(attractionSuggestion.getSuggestion_id())) {
            attractionSuggestionRepository.delete(attractionSuggestion);
            return ResponseEntity.ok().body("Suggestion discarded");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Could not delete suggestion");
        }
    }

    @PostMapping("/review/block")
    ResponseEntity<?> blockReview(@RequestParam long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElse(null);
        if (review == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Review not found");
        review.setReported(false);
        review.setBlocked(true);
        reviewRepository.save(review);
        return ResponseEntity.ok().body(review);
    }

    @PostMapping("/review/allow")
    ResponseEntity<?> allowReview(@RequestParam long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElse(null);
        if (review == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Review not found");
        review.setReported(false);
        reviewRepository.save(review);
        return ResponseEntity.ok().body(review);
    }

    @PostMapping("/comment/block")
    ResponseEntity<?> blockComment(@RequestParam long commentId) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment not found");
        comment.setReported(false);
        comment.setBlocked(true);
        commentRepository.save(comment);
        return ResponseEntity.ok().body(comment);
    }

    @PostMapping("/comment/allow")
    ResponseEntity<?> allowComment(@RequestParam long commentId) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if(comment == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment not found");
        comment.setReported(false);
        commentRepository.save(comment);
        return ResponseEntity.ok().body(comment);
    }
}
