package com.inzynierka.RatingTouristAttractions.Controllers;

import com.inzynierka.RatingTouristAttractions.Entities.Attraction;
import com.inzynierka.RatingTouristAttractions.Repositories.AttractionRepository;
import com.inzynierka.RatingTouristAttractions.Requests.AttractionRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/attractions")
public class AttractionController {

    private final AttractionRepository attractionRepository;

    public AttractionController(AttractionRepository attractionRepository) {
        this.attractionRepository = attractionRepository;
    }

    @GetMapping
    public List<Attraction> getAllAttractions() { return attractionRepository.findAll(); }

    @GetMapping("/{id}")
    public Attraction getAttractionById(@PathVariable long id) {
        return attractionRepository.findById(id).orElse(null);
    }

    @GetMapping("/search/{name}")
    List<Attraction> searchAttractionLike(@PathVariable String name) {
        return attractionRepository.findByNameLike(name);
    }

    @PostMapping("/add")
    public Attraction addAttraction(@RequestBody AttractionRequest attractionRequest) {
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
    public void deleteAttractionById(@PathVariable long id) {
        attractionRepository.deleteById(id);
    }
}
