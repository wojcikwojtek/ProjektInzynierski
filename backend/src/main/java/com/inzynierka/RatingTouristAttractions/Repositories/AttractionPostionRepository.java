package com.inzynierka.RatingTouristAttractions.Repositories;

import com.inzynierka.RatingTouristAttractions.Entities.AttractionPosition;
import com.inzynierka.RatingTouristAttractions.Entities.AttractionPositionKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttractionPostionRepository extends JpaRepository<AttractionPosition, AttractionPositionKey> {
}
