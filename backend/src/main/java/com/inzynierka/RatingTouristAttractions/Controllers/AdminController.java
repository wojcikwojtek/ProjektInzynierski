package com.inzynierka.RatingTouristAttractions.Controllers;

import com.inzynierka.RatingTouristAttractions.Entities.AttractionSuggestion;
import com.inzynierka.RatingTouristAttractions.Repositories.AttractionSuggestionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AttractionSuggestionRepository attractionSuggestionRepository;

    public AdminController(AttractionSuggestionRepository attractionSuggestionRepository) {
        this.attractionSuggestionRepository = attractionSuggestionRepository;
    }

    @GetMapping("/suggestions")
    List<AttractionSuggestion> getSuggestions() { return attractionSuggestionRepository.findAll(); }

    @PostMapping("/addsuggestion")
    ResponseEntity<?> addSuggestion(@RequestPart("suggestion") AttractionSuggestion suggestion, @RequestPart("file") MultipartFile file) {
        AttractionSuggestion attractionSuggestion = attractionSuggestionRepository.save(suggestion);
        try {
            FileController fileController = new FileController();
            fileController.uploadSuggestion(file, attractionSuggestion.getSuggestion_id());
            return ResponseEntity.ok().body(attractionSuggestion);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
