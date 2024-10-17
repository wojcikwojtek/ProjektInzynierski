package com.inzynierka.RatingTouristAttractions.Controllers;

import com.inzynierka.RatingTouristAttractions.Entities.Attraction;
import com.inzynierka.RatingTouristAttractions.Entities.AttractionSuggestion;
import com.inzynierka.RatingTouristAttractions.Entities.User;
import com.inzynierka.RatingTouristAttractions.Repositories.AttractionRepository;
import com.inzynierka.RatingTouristAttractions.Repositories.AttractionSuggestionRepository;
import com.inzynierka.RatingTouristAttractions.Repositories.UserRepository;
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
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AttractionSuggestionRepository attractionSuggestionRepository;
    private final UserRepository userRepository;
    private final AttractionRepository attractionRepository;

    public AdminController(AttractionSuggestionRepository attractionSuggestionRepository, UserRepository userRepository, AttractionRepository attractionRepository) {
        this.attractionSuggestionRepository = attractionSuggestionRepository;
        this.userRepository = userRepository;
        this.attractionRepository = attractionRepository;
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
}
