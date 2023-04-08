package com.mile.nokenzivot.base;

import com.mile.nokenzivot.global.dto.Coordinates;
import com.mile.nokenzivot.global.entities.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Repository
@Transactional(readOnly = true)
interface ClubRepository extends JpaRepository<Club, Long> {


  @Query("SELECT c.latitude as latitude, c.longitude as longitude FROM Club c WHERE c.genre = ?1 AND c.averageCost = ?2")
  Set<Coordinates> findByGenreAndAveragePrice(String genre, String averagePrice);

  @Query("SELECT c.latitude as latitude, c.longitude as longitude FROM Club c WHERE c.genre = ?1")
  Set<Coordinates> findByGenre(String genre);

  @Query("SELECT c.latitude as latitude, c.longitude as longitude FROM Club c WHERE c.averageCost = ?1")
  Set<Coordinates> findByAveragePrice(String averagePrice);

  @Query("SELECT c FROM Club c WHERE c.email = ?1")
  Optional<Club> findByEmail(String sender);

  @Query("SELECT c.latitude as latitude, c.longitude as longitude FROM Club c")
  Set<Coordinates> getAllCoordinatesForClubs();

  @Query("SELECT c FROM Club c WHERE c.latitude = ?1 AND c.longitude = ?2")
  Club findByCoordinates(double latitude, double longitude);

  @Query("SELECT c.name as name, c.genre as genre,"
      + " c.averageCost as averageCost, c.rating as rating"
      + " FROM Club c WHERE c.latitude = ?1 AND c.longitude = ?2")
  OnHoverClub findByCoordinatesHover(double latitude, double longitude);
}
