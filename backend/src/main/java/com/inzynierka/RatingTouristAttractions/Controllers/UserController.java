package com.inzynierka.RatingTouristAttractions.Controllers;

import com.inzynierka.RatingTouristAttractions.Entities.User;
import com.inzynierka.RatingTouristAttractions.Repositories.UserRepository;
import com.inzynierka.RatingTouristAttractions.Requests.UserRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{login}")
    User getUserByLogin(@PathVariable String login) {
        return userRepository.findByLogin(login);
    }

    @PostMapping("/register")
    ResponseEntity<?> registerUser(@RequestBody UserRequest userRequest) {
        if(userRepository.findByLogin(userRequest.getLogin()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
        }
        if(userRepository.findByEmail(userRequest.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already in use");
        }
        User user = new User(userRequest.getLogin(), userRequest.getPassword(), userRequest.getEmail());
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @DeleteMapping("/{login}")
    void deleteUser(@PathVariable String login) { userRepository.deleteByLogin(login); }
}
