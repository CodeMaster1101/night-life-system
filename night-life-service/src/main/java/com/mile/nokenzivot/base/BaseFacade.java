package com.mile.nokenzivot.base;

import com.mile.nokenzivot.global.dto.Coordinates;
import com.mile.nokenzivot.global.entities.Club;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class BaseFacade {

  private final ClubRepository clubRepository;

  public BaseFacade(ClubRepository clubRepository) {
    this.clubRepository = clubRepository;
  }

  public Club getClubByEmail(String sender) {
    Optional<Club> club = clubRepository.findByEmail(sender);
    if (club.isPresent()) {
      return club.get();
    } throw new NightLifeException("Club was not present for this email", new RuntimeException());
  }

  public Set<Coordinates> getAllClubsCoordinates() {
    return clubRepository.getAllCoordinatesForClubs();
  }
}
