package com.inzynierka.RatingTouristAttractions.Repositories;

import com.inzynierka.RatingTouristAttractions.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByLogin(String login);
    User findByEmail(String email);
    void deleteByLogin(String login);
    @Query(value = "SELECT Count(*) FROM user_following where followed_user_id = ?",nativeQuery = true)
    int countFollowers(Long userId);
    @Query(value = "SELECT u.user_id, u.login, u.password, u.email FROM users u JOIN user_following f ON u.user_id = f.user_id WHERE f.followed_user_id = ?", nativeQuery = true)
    List<User> findFollowers(Long userId);
}
