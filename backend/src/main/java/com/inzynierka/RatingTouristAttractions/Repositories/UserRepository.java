package com.inzynierka.RatingTouristAttractions.Repositories;

import com.inzynierka.RatingTouristAttractions.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByLogin(String login);
    User findByEmail(String email);
    void deleteByLogin(String login);
}
