package com.mile.nokenzivot.base;

import com.mile.nokenzivot.global.dto.Coordinates;
import com.mile.nokenzivot.global.entities.Club;
import com.mile.nokenzivot.global.entities.PartyEvent;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Optional;
import java.util.Set;

@Service
public class BaseFacade {

  private final ClubRepository clubRepository;
  private final PartyEventRepository partyEventRepository;

  public BaseFacade(ClubRepository clubRepository, PartyEventRepository partyEventRepository) {
    this.clubRepository = clubRepository;
    this.partyEventRepository = partyEventRepository;
  }

  public Club getClubByEmail(String sender) {
    Optional<Club> club = clubRepository.findByEmail(sender);
    if (club.isPresent()) {
      return club.get();
    } throw new NightLifeException("Club was not present for this email", new RuntimeException());
  }

  public Optional<PartyEvent> getEventByDateAndClub(Date date, Club club) {
    return partyEventRepository.findByDateAndClub(date, club);
  }

  public Set<Coordinates> getAllClubsCoordinates() {
    return clubRepository.getAllCoordinatesForClubs();
  }
}
