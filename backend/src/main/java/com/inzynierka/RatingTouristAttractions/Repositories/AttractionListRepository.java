package com.inzynierka.RatingTouristAttractions.Repositories;

import com.inzynierka.RatingTouristAttractions.Entities.AttractionList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttractionListRepository extends JpaRepository<AttractionList, Long> {
    @Query(value = "SELECT * FROM lists order by publication_date desc", nativeQuery = true)
    List<AttractionList> findAllOrderByPublicationDate();
}
