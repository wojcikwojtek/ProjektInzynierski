package com.inzynierka.RatingTouristAttractions.Controllers;

import com.inzynierka.RatingTouristAttractions.Dtos.ReviewDto;
import com.inzynierka.RatingTouristAttractions.Entities.Attraction;
import com.inzynierka.RatingTouristAttractions.Entities.Review;
import com.inzynierka.RatingTouristAttractions.Repositories.AttractionRepository;
import com.inzynierka.RatingTouristAttractions.Requests.AttractionRequest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.text.similarity.LevenshteinDistance;

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

    @GetMapping("/{id}/reviews/{index}")
    ResponseEntity<?> getAttractionReviews(@PathVariable long id, @PathVariable int index) {
        Attraction attraction = attractionRepository.findById(id).orElse(null);
        if (attraction == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Attraction not found");
        List<ReviewDto> reviews = new ArrayList<>();
        List<Review> reviewList = attraction.getReviews()
                .stream()
                .filter(review -> !review.isBlocked())
                .collect(Collectors.toList());
        if(index >= reviewList.size()) return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<>());
        if(index == 0) {
            if(reviewList.size() == 1) {
                reviews.add(new ReviewDto(reviewList.get(0)));
            } else {
                reviews.add(new ReviewDto(reviewList.get(0)));
                reviews.add(new ReviewDto(reviewList.get(1)));
            }
        } else {
            for(int i = index; i < index + 5; i++) {
                if(i >= reviewList.size()) break;

                reviews.add(new ReviewDto(reviewList.get(i)));
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(reviews);
    }

    @GetMapping("/search/{name}")
    List<Attraction> searchAttractionLike(@PathVariable String name) {
        List<Attraction> attractions = attractionRepository.findAll();
        Map<Attraction, Integer> foundAttractions = new HashMap<>();
        LevenshteinDistance distance = new LevenshteinDistance();
        for (Attraction attraction : attractions) {
            int minDistance = Math.min(
                    Math.min(distance.apply(name, attraction.getName()), distance.apply(name, attraction.getCountry())),
                    distance.apply(name, attraction.getCity())
            );
            if(minDistance <= 3 ||
                    attraction.getName().toLowerCase().contains(name.toLowerCase()) || name.toLowerCase().contains(attraction.getName().toLowerCase()) ||
                    attraction.getCountry().toLowerCase().contains(name.toLowerCase()) || name.toLowerCase().contains(attraction.getCountry().toLowerCase()) ||
                    attraction.getCity().toLowerCase().contains(name.toLowerCase()) || name.toLowerCase().contains(attraction.getCity().toLowerCase())) {
                foundAttractions.put(attraction, minDistance);
            }
        }
        return foundAttractions.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}/image")
    ResponseEntity<?> getAttractionImage(@PathVariable long id) {
        try {
            Path path = Paths.get("src/main/resources/images/attraction/Attraction" + id + ".jpg");
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

    @PostMapping("/add")
    Attraction addAttraction(@RequestBody AttractionRequest attractionRequest) {
        Attraction attraction = new Attraction(
                attractionRequest.getName(),
                attractionRequest.getCountry(),
                attractionRequest.getCity(),
                attractionRequest.getLocation(),
                attractionRequest.getDescription()
        );
        attractionRepository.save(attraction);
        return attraction;
    }

    @DeleteMapping("/{id}")
    void deleteAttractionById(@PathVariable long id) {
        attractionRepository.deleteById(id);
    }
}
