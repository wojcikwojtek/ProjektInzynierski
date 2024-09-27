package com.inzynierka.RatingTouristAttractions.Controllers;

import com.inzynierka.RatingTouristAttractions.Dtos.AttractionListDto;
import com.inzynierka.RatingTouristAttractions.Entities.*;
import com.inzynierka.RatingTouristAttractions.Repositories.AttractionListRepository;
import com.inzynierka.RatingTouristAttractions.Repositories.AttractionPostionRepository;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/lists")
public class AttractionListController {

    private final AttractionListRepository attractionListRepository;
    private final AttractionRepository attractionRepository;
    private final UserRepository userRepository;
    private final AttractionPostionRepository attractionPostionRepository;

    public AttractionListController(AttractionListRepository attractionListRepository, AttractionRepository attractionRepository, UserRepository userRepository, AttractionPostionRepository attractionPostionRepository) {
        this.attractionListRepository = attractionListRepository;
        this.attractionRepository = attractionRepository;
        this.userRepository = userRepository;
        this.attractionPostionRepository = attractionPostionRepository;
    }

    @GetMapping
    List<AttractionList> getAllLists() { return attractionListRepository.findAll(); }

    @GetMapping("/{id}")
    AttractionListDto getListById(@PathVariable long id) {
        AttractionList attractionList = attractionListRepository.findById(id).orElse(null);
        if(attractionList == null) return null;
        String dateTime = attractionList.getPublicationDate()
                .format(
                        DateTimeFormatter
                                .ofLocalizedDateTime(FormatStyle.SHORT)
                                .withLocale(
                                        new Locale("pl", "PL")
                                )
                );
        return new AttractionListDto(
                attractionList.getList_id(),
                attractionList.getName(),
                attractionList.getDescription(),
                dateTime,
                attractionList.getSize(),
                attractionList.getUser()
        );
    }

    @GetMapping("/{id}/attractions")
    List<AttractionPosition> getListAttractions(@PathVariable long id) {
        AttractionList attractionList = attractionListRepository.findById(id).orElse(null);
        if (attractionList == null) return null;
        return attractionList.getAttractions()
                .stream()
                .sorted((o1, o2) -> Integer.compare(o1.getPosition(), o2.getPosition()))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}/likecount")
    int getListLikeCount(@PathVariable long id) {
        AttractionList attractionList = attractionListRepository.findById(id).orElse(null);
        if (attractionList == null) return -1;
        return attractionList.getLikes().size();
    }

    @GetMapping("/{index}/mostpopular")
    List<AttractionList> getTenMostPopularLists(@PathVariable int index) {
        List<AttractionList> attractionLists = attractionListRepository.findAll();
        if (attractionLists.isEmpty() || attractionLists.size() <= index) return new ArrayList<>();
        attractionLists = attractionLists
                .stream()
                .sorted((o1, o2) -> Integer.compare(o2.getLikes().size(), o1.getLikes().size()))
                .collect(Collectors.toList());
        if(index + 10 > attractionLists.size()) return attractionLists.subList(index, attractionLists.size());
        return attractionLists.subList(index, index + 10);
    }

    @GetMapping("/{index}/newest")
    List<AttractionList> getTenNewestLists(@PathVariable int index) {
        List<AttractionList> attractionLists = attractionListRepository.findAllOrderByPublicationDate();
        if (attractionLists.isEmpty() || attractionLists.size() <= index) return new ArrayList<>();
        if(index + 10 > attractionLists.size()) return attractionLists.subList(index, attractionLists.size());
        return attractionLists.subList(index, index + 10);
    }

    @GetMapping("/{index}/friendslists/{userId}")
    List<AttractionList> getTenFriendsLists(@PathVariable long userId, @PathVariable int index) {
        User user = userRepository.findById(userId).orElse(null);
        if(user == null) return new ArrayList<>();
        List<AttractionList> friendsLists = user.getFollowedUsers().stream()
                .flatMap(followedUser -> followedUser.getLists().stream())
                .collect(Collectors.toList());
        if(friendsLists.isEmpty() || friendsLists.size() <= index) return new ArrayList<>();
        friendsLists = friendsLists.stream()
                .sorted((o1, o2) -> Integer.compare(o2.getLikes().size(), o1.getLikes().size()))
                .collect(Collectors.toList());
        if(index + 10 > friendsLists.size()) return friendsLists.subList(index, friendsLists.size());
        return  friendsLists.subList(index, index + 10);
    }

    @PostMapping("/create")
    ResponseEntity<?> createList(@RequestBody ListRequest listRequest) {
        User user = userRepository.findById(listRequest.getUserId()).orElse(null);
        if(user == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

        AttractionList list = new AttractionList(
                listRequest.getName(),
                listRequest.getDescription(),
                user
        );

        List<AttractionPosition> attractions = new ArrayList<>();
        for(int i = 0; i < listRequest.getAttractionIds().size(); i++) {
            long id = listRequest.getAttractionIds().get(i);
            Attraction attraction = attractionRepository.findById(id).orElse(null);
            if(attraction == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Attraction " + id + " not found");
            attractions.add(new AttractionPosition(attraction, list, i));
        }
        list.setSize(list.getAttractions().size());

        attractionListRepository.save(list);
        return ResponseEntity.status(HttpStatus.CREATED).body(list);
    }

    @PostMapping("/add")
    ResponseEntity<?> addAttraction(@RequestBody AddToListRequest addToListRequest) {
        AttractionList attractionList = attractionListRepository.findById(addToListRequest.getListId()).orElse(null);
        if(attractionList == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("List not found");

        Attraction attraction = attractionRepository.findById(addToListRequest.getAttractionId()).orElse(null);
        if(attraction == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Attraction not found");

        for(AttractionPosition attractionPosition : attractionList.getAttractions()) {
            if(attraction == attractionPosition.getAttraction()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Attraction already exists");
            }
        }
        AttractionPosition attractionPosition = new AttractionPosition(attraction, attractionList, attractionList.getSize());
        attractionList.setSize(attractionList.getAttractions().size() + 1);

        attractionPostionRepository.save(attractionPosition);
        attractionListRepository.save(attractionList);
        return ResponseEntity.status(HttpStatus.OK).body(attractionList);
    }

    @PutMapping("/editentries")
    ResponseEntity<?> updateEntries(@RequestBody EditListRequest editListRequest) {
        AttractionList attractionList = attractionListRepository.findById(editListRequest.getListId()).orElse(null);
        if(attractionList == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("List not found");
        attractionPostionRepository.saveAll(editListRequest.getAttractions());
        return ResponseEntity.status(HttpStatus.OK).body(attractionList);
    }

    @DeleteMapping("/{id}")
    void deleteListById(@PathVariable long id) { attractionListRepository.deleteById(id); }

    @DeleteMapping("/{id}/entry/{attractionId}")
    void deleteListPositionById(@PathVariable long id, @PathVariable long attractionId) {
        attractionPostionRepository.deleteById(new AttractionPositionKey(attractionId, id));
        AttractionList attractionList = attractionListRepository.findById(attractionId).orElse(null);
        if(attractionList == null) return;
        attractionList.setSize(attractionList.getAttractions().size());
        attractionListRepository.save(attractionList);
    }
}
