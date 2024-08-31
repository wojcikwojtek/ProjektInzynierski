package com.inzynierka.RatingTouristAttractions.Controllers;

import com.inzynierka.RatingTouristAttractions.Entities.Comment;
import com.inzynierka.RatingTouristAttractions.Entities.Review;
import com.inzynierka.RatingTouristAttractions.Entities.User;
import com.inzynierka.RatingTouristAttractions.Repositories.CommentRepository;
import com.inzynierka.RatingTouristAttractions.Repositories.ReviewRepository;
import com.inzynierka.RatingTouristAttractions.Repositories.UserRepository;
import com.inzynierka.RatingTouristAttractions.Requests.CommentRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    public CommentController(CommentRepository commentRepository, UserRepository userRepository, ReviewRepository reviewRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
    }

    @GetMapping
    List<Comment> getAllComments() { return commentRepository.findAll(); }

    @GetMapping("/{id}")
    Comment getCommentById(@PathVariable long id) { return commentRepository.findById(id).orElse(null); }

    @PostMapping
    ResponseEntity<?> addComment(@RequestBody CommentRequest commentRequest) {
        User user = userRepository.findById(commentRequest.getUserId()).orElse(null);
        if (user == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        Review review = reviewRepository.findById(commentRequest.getReviewId()).orElse(null);
        if (review == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Review not found");
        String dateTime = LocalDateTime
                .now().format(
                        DateTimeFormatter
                                .ofLocalizedDateTime(FormatStyle.SHORT)
                                .withLocale(
                                        new Locale("pl", "PL")
                                )
                );
        Comment comment = new Comment(
                commentRequest.getContents(),
                dateTime,
                user,
                review
        );
        commentRepository.save(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    @DeleteMapping("/{id}")
    void deleteCommentById(@PathVariable long id) { commentRepository.deleteById(id); }
}
