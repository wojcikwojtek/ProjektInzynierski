package com.inzynierka.RatingTouristAttractions.Controllers;

import com.inzynierka.RatingTouristAttractions.Entities.Attraction;
import com.inzynierka.RatingTouristAttractions.Entities.AttractionList;
import com.inzynierka.RatingTouristAttractions.Entities.User;
import com.inzynierka.RatingTouristAttractions.Repositories.AttractionListRepository;
import com.inzynierka.RatingTouristAttractions.Repositories.AttractionRepository;
import com.inzynierka.RatingTouristAttractions.Repositories.UserRepository;
import com.inzynierka.RatingTouristAttractions.Requests.AddToListRequest;
import com.inzynierka.RatingTouristAttractions.Requests.AttractionRequest;
import com.inzynierka.RatingTouristAttractions.Requests.EditListRequest;
import com.inzynierka.RatingTouristAttractions.Requests.ListRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/lists")
public class AttractionListController {

    private final AttractionListRepository attractionListRepository;
    private final AttractionRepository attractionRepository;
    private final UserRepository userRepository;

    public AttractionListController(AttractionListRepository attractionListRepository, AttractionRepository attractionRepository, UserRepository userRepository) {
        this.attractionListRepository = attractionListRepository;
        this.attractionRepository = attractionRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    List<AttractionList> getAllLists() { return attractionListRepository.findAll(); }

    @GetMapping("/{id}")
    AttractionList getListById(@PathVariable long id) { return attractionListRepository.findById(id).orElse(null); }

    @GetMapping("/{id}/attractions")
    List<Attraction> getListAttractions(@PathVariable long id) {
        AttractionList attractionList = attractionListRepository.findById(id).orElse(null);
        if (attractionList == null) return null;
        return attractionList.getAttractions();
    }

    @GetMapping("/{id}/likecount")
    int getListLikeCount(@PathVariable long id) {
        AttractionList attractionList = attractionListRepository.findById(id).orElse(null);
        if (attractionList == null) return -1;
        return attractionList.getLikes().size();
    }

    @PostMapping("/create")
    ResponseEntity<?> createList(@RequestBody ListRequest listRequest) {
        User user = userRepository.findById(listRequest.getUserId()).orElse(null);
        if(user == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

        List<Attraction> attractions = new ArrayList<>();
        for(long id : listRequest.getAttractionIds()) {
            Attraction attraction = attractionRepository.findById(id).orElse(null);
            if(attraction == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Attraction " + id + " not found");
            attractions.add(attraction);
        }

        String dateTime = LocalDateTime
                .now().format(
                        DateTimeFormatter
                                .ofLocalizedDateTime(FormatStyle.SHORT)
                                .withLocale(
                                        new Locale("pl", "PL")
                                )
                );

        AttractionList list = new AttractionList(
                listRequest.getName(),
                dateTime,
                listRequest.getDescription(),
                user,
                attractions
        );

        attractionListRepository.save(list);
        return ResponseEntity.status(HttpStatus.CREATED).body(list);
    }

    @PostMapping("/add")
    ResponseEntity<?> addAttraction(@RequestBody AddToListRequest addToListRequest) {
        AttractionList attractionList = attractionListRepository.findById(addToListRequest.getListId()).orElse(null);
        if(attractionList == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("List not found");

        Attraction attraction = attractionRepository.findById(addToListRequest.getAttractionId()).orElse(null);
        if(attraction == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Attraction not found");

        if(attractionList.getAttractions().contains(attraction)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Attraction already exists");
        }
        attractionList.getAttractions().add(attraction);
        attractionList.setSize(attractionList.getAttractions().size());
        attraction.getAttractionLists().add(attractionList);

        attractionListRepository.save(attractionList);
        attractionRepository.save(attraction);
        return ResponseEntity.status(HttpStatus.OK).body(attractionList);
    }

    @PutMapping("/editentries")
    ResponseEntity<?> updateEntries(@RequestBody EditListRequest editListRequest) {
        AttractionList attractionList = attractionListRepository.findById(editListRequest.getListId()).orElse(null);
        if(attractionList == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("List not found");
        attractionList.setAttractions(editListRequest.getAttractions());
        attractionListRepository.save(attractionList);
        return ResponseEntity.status(HttpStatus.OK).body(attractionList.getAttractions());
    }

    @DeleteMapping("/{id}")
    void deleteListById(@PathVariable long id) { attractionListRepository.deleteById(id); }
}
