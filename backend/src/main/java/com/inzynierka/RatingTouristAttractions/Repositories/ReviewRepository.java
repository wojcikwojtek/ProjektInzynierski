package com.inzynierka.RatingTouristAttractions.Repositories;

import com.inzynierka.RatingTouristAttractions.Entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
}
