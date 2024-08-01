package com.inzynierka.RatingTouristAttractions.Controllers;

import com.inzynierka.RatingTouristAttractions.Entities.User;
import com.inzynierka.RatingTouristAttractions.Repositories.UserRepository;
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

    @DeleteMapping("/{login}")
    void deleteUser(@PathVariable String login) { userRepository.deleteByLogin(login); }
}
