package com.inzynierka.RatingTouristAttractions.Repositories;

import com.inzynierka.RatingTouristAttractions.Entities.Like;
import com.inzynierka.RatingTouristAttractions.Entities.Review;
import com.inzynierka.RatingTouristAttractions.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Like findByUserAndReview(User user, Review review);
}
