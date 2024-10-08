package com.inzynierka.RatingTouristAttractions.Dtos;

import com.inzynierka.RatingTouristAttractions.Entities.AttractionList;
import com.inzynierka.RatingTouristAttractions.Entities.User;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

@Getter
@Setter
public class AttractionListDto {
    private long list_id;
    private String name;
    private String description;
    private String publicationDate;
    private int size;
    private User user;

    public AttractionListDto(AttractionList attractionList) {
        this.list_id = attractionList.getList_id();
        this.name = attractionList.getName();
        this.description = attractionList.getDescription();
        this.publicationDate = attractionList.getPublicationDate()
                .format(
                        DateTimeFormatter
                                .ofLocalizedDateTime(FormatStyle.SHORT)
                                .withLocale(
                                        new Locale("pl", "PL")
                                )
                );;
        this.size = attractionList.getSize();
        this.user = attractionList.getUser();
    }
}
