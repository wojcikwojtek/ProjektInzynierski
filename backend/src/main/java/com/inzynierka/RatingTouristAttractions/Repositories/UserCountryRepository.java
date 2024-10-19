package com.inzynierka.RatingTouristAttractions.Repositories;

import com.inzynierka.RatingTouristAttractions.Entities.UserCountry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCountryRepository extends JpaRepository<UserCountry, Long> {
}
