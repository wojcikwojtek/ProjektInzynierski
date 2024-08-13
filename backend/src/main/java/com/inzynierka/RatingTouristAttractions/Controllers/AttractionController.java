package com.inzynierka.RatingTouristAttractions.Controllers;

import com.inzynierka.RatingTouristAttractions.Entities.Attraction;
import com.inzynierka.RatingTouristAttractions.Entities.Review;
import com.inzynierka.RatingTouristAttractions.Repositories.AttractionRepository;
import com.inzynierka.RatingTouristAttractions.Requests.AttractionRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

@RestController
@RequestMapping("/attractions")
public class AttractionController {

    private final AttractionRepository attractionRepository;

    public AttractionController(AttractionRepository attractionRepository) {
        this.attractionRepository = attractionRepository;
    }

    @GetMapping
    List<Attraction> getAllAttractions() { return attractionRepository.findAll(); }

    @GetMapping("/{id}")
    Attraction getAttractionById(@PathVariable long id) {
        return attractionRepository.findById(id).orElse(null);
    }

    @GetMapping("/{id}/reviews")
    ResponseEntity<?> getAttractionReviews(@PathVariable long id) {
        Attraction attraction = attractionRepository.findById(id).orElse(null);
        if (attraction == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Attraction not found");
        List reviews = new ArrayList(attraction.getReviews());
        if(reviews.size() > 0) {
            Collections.sort(reviews, new Comparator<Review>() {
                @Override
                public int compare(Review o1, Review o2) {
                    DateTimeFormatter formatter = DateTimeFormatter
                            .ofLocalizedDateTime(FormatStyle.SHORT)
                            .withLocale(
                                    new Locale("pl", "PL")
                            );
                    LocalDateTime t1 = LocalDateTime.parse(o1.getPublicationDate(), formatter);
                    LocalDateTime t2 = LocalDateTime.parse(o2.getPublicationDate(), formatter);
                    return t1.compareTo(t2);
                }
            });
        }
        return ResponseEntity.status(HttpStatus.OK).body(reviews);
    }

    @GetMapping("/search/{name}")
    List<Attraction> searchAttractionLike(@PathVariable String name) {
        return attractionRepository.findByNameLike(name);
    }

    @PostMapping("/add")
    Attraction addAttraction(@RequestBody AttractionRequest attractionRequest) {
        Attraction attraction = new Attraction(
                attractionRequest.getName(),
                attractionRequest.getCountry(),
                attractionRequest.getCity(),
                attractionRequest.getLocation(),
                attractionRequest.getDescription(),
                attractionRequest.getImageUrl()
        );
        attractionRepository.save(attraction);
        return attraction;
    }

    @DeleteMapping("/{id}")
    void deleteAttractionById(@PathVariable long id) {
        attractionRepository.deleteById(id);
    }
}
