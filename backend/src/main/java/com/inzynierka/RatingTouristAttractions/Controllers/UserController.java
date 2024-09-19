package com.inzynierka.RatingTouristAttractions.Controllers;

import com.inzynierka.RatingTouristAttractions.Entities.AttractionList;
import com.inzynierka.RatingTouristAttractions.Entities.Review;
import com.inzynierka.RatingTouristAttractions.Entities.User;
import com.inzynierka.RatingTouristAttractions.Dtos.ReviewDto;
import com.inzynierka.RatingTouristAttractions.Repositories.UserRepository;
import com.inzynierka.RatingTouristAttractions.Requests.LoginRequest;
import com.inzynierka.RatingTouristAttractions.Requests.RegisterRequest;
import com.inzynierka.RatingTouristAttractions.Responses.UserStatsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    List<User> getAllUsers() { return userRepository.findAll(); }

    @GetMapping("/{id}")
    User getUser(@PathVariable long id) {
        return userRepository.findById(id).orElse(null);
    }

    @GetMapping("/{login}/login")
    User getUserByLogin(@PathVariable String login) {
        return userRepository.findByLogin(login);
    }

    @GetMapping("/{id}/reviews")
    ResponseEntity<?> getUserReviews(@PathVariable long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        List<ReviewDto> reviewsDto = new ArrayList<>();
        for (Review review : user.getReviews()) {
            reviewsDto.add(new ReviewDto(
                    review,
                    review.getAttraction().getAttraction_id(),
                    review.getAttraction().getName(),
                    review.getAttraction().getImageUrl()
            ));
        }
        return ResponseEntity.status(HttpStatus.OK).body(reviewsDto);
    }

    @GetMapping("{id}/lists")
    ResponseEntity<?> getUserLists(@PathVariable long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        List<AttractionList> lists = user.getLists();
        return ResponseEntity.status(HttpStatus.OK).body(lists);
    }

    @GetMapping("/{id}/stats")
    ResponseEntity<?> getUserStats(@PathVariable long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        List<ReviewDto> recentlyReviewed = new ArrayList<>();
        int index = user.getReviews().size() - 1;
        for(int i = 0; i <=4; i++) {
            if(index < 0) break;
            recentlyReviewed.add(new ReviewDto(
                    user.getReviews().get(index),
                    user.getReviews().get(index).getAttraction().getAttraction_id(),
                    user.getReviews().get(index).getAttraction().getName(),
                    user.getReviews().get(index).getAttraction().getImageUrl()
            ));
            index--;
        }
        UserStatsResponse userStats = new UserStatsResponse(
                user.getReviews().size(),
                user.getLists().size(),
                recentlyReviewed
        );
        return ResponseEntity.status(HttpStatus.OK).body(userStats);
    }

    @PostMapping("/register")
    ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        if(userRepository.findByLogin(registerRequest.getLogin()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
        }
        if(userRepository.findByEmail(registerRequest.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already in use");
        }
        User user = new User(registerRequest.getLogin(), registerRequest.getPassword(), registerRequest.getEmail());
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/login")
    ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        User user = userRepository.findByLogin(loginRequest.getLogin());
        if(user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username not found");
        }
        if(!user.getPassword().equals(loginRequest.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong password");
        }
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @DeleteMapping("/{id}")
    void deleteUser(@PathVariable long id) { userRepository.deleteById(id); }
}
