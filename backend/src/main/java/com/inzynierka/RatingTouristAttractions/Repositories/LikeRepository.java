package com.inzynierka.RatingTouristAttractions.Repositories;

import com.inzynierka.RatingTouristAttractions.Entities.AttractionList;
import com.inzynierka.RatingTouristAttractions.Entities.Like;
import com.inzynierka.RatingTouristAttractions.Entities.Review;
import com.inzynierka.RatingTouristAttractions.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    @Query(value = "SELECT Count(*) FROM likes where user_id = ? and review_id is not null",nativeQuery = true)
    int countUserLikesByReview(Long userId);
    @Query(value = "SELECT Count(*) FROM likes where user_id = ? and list_id is not null",nativeQuery = true)
    int countUserLikesByList(Long userId);
    Like findByReviewAndUser(Review review, User user);
    Like findByListAndUser(AttractionList list, User user);
}
