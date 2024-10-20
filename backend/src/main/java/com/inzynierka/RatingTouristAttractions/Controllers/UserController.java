package com.inzynierka.RatingTouristAttractions.Controllers;

import com.inzynierka.RatingTouristAttractions.Dtos.AttractionListWithImagesDto;
import com.inzynierka.RatingTouristAttractions.Dtos.ReviewDto;
import com.inzynierka.RatingTouristAttractions.Dtos.UserDto;
import com.inzynierka.RatingTouristAttractions.Entities.*;
import com.inzynierka.RatingTouristAttractions.Dtos.ReviewWithImageDto;
import com.inzynierka.RatingTouristAttractions.Repositories.UserRepository;
import com.inzynierka.RatingTouristAttractions.Requests.FollowUserRequest;
import com.inzynierka.RatingTouristAttractions.Requests.LoginRequest;
import com.inzynierka.RatingTouristAttractions.Requests.RegisterRequest;
import com.inzynierka.RatingTouristAttractions.Requests.UserUpdateRequest;
import com.inzynierka.RatingTouristAttractions.Responses.UserStatsResponse;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.stream.Collectors;

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
        List<ReviewWithImageDto> reviewsDto = new ArrayList<>();
        for (Review review : user.getReviews()) {
            reviewsDto.add(new ReviewWithImageDto(
                    new ReviewDto(review),
                    review.getAttraction().getAttraction_id(),
                    review.getAttraction().getName()
            ));
        }
        return ResponseEntity.status(HttpStatus.OK).body(reviewsDto);
    }

    @GetMapping("{id}/lists")
    ResponseEntity<?> getUserLists(@PathVariable long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        List<AttractionListWithImagesDto> listsWithImages = new ArrayList<>();
        for(AttractionList list : user.getLists()) {
            List<AttractionPosition> attractions = list.getAttractions()
                    .stream()
                    .sorted((o1, o2) -> Integer.compare(o1.getPosition(), o2.getPosition()))
                    .collect(Collectors.toList());
            List<Long> topFourImages = new ArrayList<>();
            for(int i = 0; i < 4; i++) {
                if(i >= attractions.size()) break;
                topFourImages.add(attractions.get(i).getAttraction().getAttraction_id());
            }
            listsWithImages.add(new AttractionListWithImagesDto(list, topFourImages));
        }
        return ResponseEntity.status(HttpStatus.OK).body(listsWithImages);
    }

    @GetMapping("/{id}/stats")
    ResponseEntity<?> getUserStats(@PathVariable long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        List<ReviewWithImageDto> recentlyReviewed = new ArrayList<>();
        List<String> visitedCountriesIds = new ArrayList<>();
        int index = user.getReviews().size() - 1;
        for(int i = 0; i <=4; i++) {
            if(index < 0) break;
            recentlyReviewed.add(new ReviewWithImageDto(
                    new ReviewDto(user.getReviews().get(index)),
                    user.getReviews().get(index).getAttraction().getAttraction_id(),
                    user.getReviews().get(index).getAttraction().getName()
            ));
            index--;
        }
        user.getVisitedCountries()
                .forEach(userCountry -> visitedCountriesIds.add(userCountry.getCountry().getCountry_id()));
        UserStatsResponse userStats = new UserStatsResponse(
                user.getReviews().size(),
                user.getLists().size(),
                user.getFollowedUsers().size(),
                userRepository.countFollowers(id),
                recentlyReviewed,
                visitedCountriesIds
        );
        return ResponseEntity.status(HttpStatus.OK).body(userStats);
    }

    @GetMapping("/{id}/following")
    ResponseEntity<?> getUserFriends(@PathVariable long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        List<User> followedUsers = user.getFollowedUsers();
        return ResponseEntity.status(HttpStatus.OK).body(followedUsers);
    }

    @GetMapping("/{id}/isfollowing/{userId}")
    boolean isUserFollowing(@PathVariable long id, @PathVariable long userId) {
        User user = userRepository.findById(id).orElse(null);
        User followedUser = userRepository.findById(userId).orElse(null);
        if (user == null || followedUser == null) return false;
        return user.getFollowedUsers().contains(followedUser);
    }

    @GetMapping("{id}/getfollowers/{userId}")
    ResponseEntity<?> getUserFollowers(@PathVariable long id, @PathVariable long userId) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        List<User> followers = userRepository.findFollowers(id);
        List<UserDto> followerDtos = new ArrayList<>();
        for(User follower : followers) {
            followerDtos.add(new UserDto(follower, this.isUserFollowing(userId, follower.getUser_id())));
        }
        return ResponseEntity.status(HttpStatus.OK).body(followerDtos);
    }

    @GetMapping("{id}/getfollowing/{userId}")
    ResponseEntity<?> getUserFollowing(@PathVariable long id, @PathVariable long userId) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        List<UserDto> followedUsersDtos = new ArrayList<>();
        for(User followedUser : user.getFollowedUsers()) {
            followedUsersDtos.add(new UserDto(followedUser, this.isUserFollowing(userId, followedUser.getUser_id())));
        }
        return ResponseEntity.status(HttpStatus.OK).body(followedUsersDtos);
    }

    @GetMapping("/{id}/recentreviews")
    ResponseEntity<?> getFollowedUsersRecentReviews(@PathVariable long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        List<ReviewWithImageDto> recentlyReviewed = new ArrayList<>();
        for(User followedUser : user.getFollowedUsers()) {
            Review recentReview = followedUser.getReviews().getLast();
            recentlyReviewed.add(new ReviewWithImageDto(
                    new ReviewDto(recentReview),
                    recentReview.getAttraction().getAttraction_id(),
                    recentReview.getAttraction().getName()
            ));
        }
        return ResponseEntity.status(HttpStatus.OK).body(recentlyReviewed);
    }

    @GetMapping("/{id}/profilepic")
    ResponseEntity<?> getUserProfilePic(@PathVariable long id) {
        try {
            Path path = Paths.get("src/main/resources/images/user/ProfilePic" + id + ".jpg");
            if(!Files.exists(path)) {
                path = Paths.get("src/main/resources/images/user/PictureNotFound.jpg");
            }
            Resource resource = new UrlResource(path.toUri());
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(resource);
        } catch (MalformedURLException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Image url is invalid");
        }
    }

    @GetMapping("{id}/search/{name}")
    List<UserDto> searchUsersLike(@PathVariable long id, @PathVariable String name) {
        User loggedUser = userRepository.findById(id).orElse(null);
        List<User> users = userRepository.findAll();
        Map<UserDto, Integer> foundUsers = new HashMap<>();
        LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
        for(User user : users) {
            if(loggedUser != null && loggedUser.getLogin().equals(user.getLogin())) continue;
            int minDistance = levenshteinDistance.apply(name, user.getLogin());
            if(minDistance <= 3 || user.getLogin().toLowerCase().contains(name.toLowerCase()) || name.toLowerCase().contains(user.getLogin().toLowerCase())) {
                boolean isFollowing = loggedUser!=null ? loggedUser.getFollowedUsers().contains(user) : false;
                foundUsers.put(new UserDto(user, isFollowing), minDistance);
            }
        }
        return foundUsers.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
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

    @PostMapping("/follow")
    ResponseEntity<?> followUser(@RequestBody FollowUserRequest followUserRequest) {
        User user = userRepository.findById(followUserRequest.getUserId()).orElse(null);
        if(user == null) ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        User followedUser = userRepository.findById(followUserRequest.getFollowedUserId()).orElse(null);
        if(followedUser == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Followed user not found");
        user.getFollowedUsers().add(followedUser);
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PutMapping("/update")
    ResponseEntity<?> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        User user = userRepository.findById(userUpdateRequest.getUserId()).orElse(null);
        if(user == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        if(userRepository.findByLogin(userUpdateRequest.getLogin()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
        }
        if(userRepository.findByEmail(userUpdateRequest.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already in use");
        }
        if(!userUpdateRequest.getLogin().isEmpty()) user.setLogin(userUpdateRequest.getLogin());
        if(!userUpdateRequest.getPassword().isEmpty()) user.setPassword(userUpdateRequest.getPassword());
        if(!userUpdateRequest.getEmail().isEmpty()) user.setEmail(userUpdateRequest.getEmail());
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @DeleteMapping("/{id}")
    void deleteUser(@PathVariable long id) { userRepository.deleteById(id); }

    @DeleteMapping("/{id}/unfollow/{userId}")
    void unfollowUser(@PathVariable long id, @PathVariable long userId) {
        User user = userRepository.findById(id).orElse(null);
        User followedUser = userRepository.findById(userId).orElse(null);
        if(user == null || followedUser == null) return;
        user.getFollowedUsers().remove(followedUser);
        userRepository.save(user);
    }
}
