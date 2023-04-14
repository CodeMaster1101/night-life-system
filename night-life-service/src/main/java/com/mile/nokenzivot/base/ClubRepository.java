package com.mile.nokenzivot.base;

import com.mile.nokenzivot.global.dto.TypedCoordinates;
import com.mile.nokenzivot.global.entities.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Repository
@Transactional(readOnly = true)
interface ClubRepository extends JpaRepository<Club, Long>, JpaSpecificationExecutor<Club> {

  @Query("SELECT c FROM Club c WHERE c.email = ?1")
  Optional<Club> findByEmail(String sender);

  @Query("SELECT new com.mile.nokenzivot.global.dto.TypedCoordinates("
      + "c.type, c.latitude, c.longitude) FROM Club c")
  Set<TypedCoordinates> getAllCoordinatesForClubs();

  @Query("SELECT new com.mile.nokenzivot.base.OnHoverClub(c.name, c.genre, c.averageCost, c.address)"
      + " FROM Club c WHERE c.latitude = ?1 AND c.longitude = ?2")
  OnHoverClub findByCoordinates(double latitude, double longitude);

  Club findByName(String placeName);
}
