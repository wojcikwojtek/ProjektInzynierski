package com.inzynierka.RatingTouristAttractions.Repositories;

import com.inzynierka.RatingTouristAttractions.Entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
