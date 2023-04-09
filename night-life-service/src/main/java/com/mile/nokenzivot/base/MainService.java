package com.mile.nokenzivot.base;
import com.mile.nokenzivot.global.dto.Coordinates;
import com.mile.nokenzivot.global.entities.Club;
import com.mile.nokenzivot.global.entities.PartyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
class MainService {

  private final ClubRepository clubRepository;
  private final PartyEventRepository partyEventRepository;

  private static final Logger logger = LoggerFactory.getLogger(MainService.class);

  MainService(
      ClubRepository clubRepository,
      PartyEventRepository partyEventRepository) {
    this.clubRepository = clubRepository;
    this.partyEventRepository = partyEventRepository;
  }

  PartyEventDTO getClubOnClick(Coordinates coordinates, String date) {
    Club club = clubRepository.findByCoordinates(
        coordinates.getLatitude(),
        coordinates.getLongitude());
    return partyEventRepository.findDtoByDateAndClub(Date.valueOf(date), club);
  }

  Set<Coordinates> filterPlacesByGenreAndPrice(String genre, String averagePrice) {
    try {
      return validateAndReturnFilteredInformation(genre, averagePrice);
    } catch (Exception e) {
      throw new NightLifeException("Something went wrong when fetching the filtered POIs, cause: ", e);
    }
  }

  private Set<Coordinates> validateAndReturnFilteredInformation(String genre, String averagePrice) {
    if (genre == null && averagePrice == null) {
      return new HashSet<>();
    } else if (genre != null && averagePrice != null) {
      return clubRepository.findByGenreAndAveragePrice(genre, averagePrice);
    } else if (genre != null) {
      return clubRepository.findByGenre(genre);
    }  else {
      return clubRepository.findByAveragePrice(averagePrice);
    }
  }

  OnHoverClub getClubOnHover(Coordinates coordinates) {
    return clubRepository.findByCoordinatesHover(coordinates.getLatitude(),
        coordinates.getLongitude());
  }

  Set<PartyEventDTO> getAllEvents(String date) {
    return partyEventRepository.findAllByDate(date);
  }

  Set<Coordinates> getAllCoordinates() {
    Set<Object[]> results = clubRepository.getAllCoordinatesForClubs();
    Set<Coordinates> coordinates = new HashSet<>();
    for (Object[] result : results) {
      double latitude = (double) result[0];
      double longitude = (double) result[1];
      coordinates.add(new Coordinates(latitude, longitude));
    }
    return coordinates;
  }

  Optional<PartyEvent> findEventByDateAndClub(Date date, Club club) {
    return partyEventRepository.findByDateAndClub(date, club);
  }

  Club getClubByEmail(String sender) {
    Optional<Club> club = clubRepository.findByEmail(sender);
    if (club.isPresent()) {
      return club.get();
    } throw new NightLifeException("Club was not present for this email", new RuntimeException());
  }

  Club saveClub(Club club) {
    return clubRepository.save(club);
  }

  void removeAllOutdatedEvents(Date date) {
    try {
      partyEventRepository.deleteAllBeforeSpecificDate(date);
      logger.info("Successfully deleted all events");
    } catch (Exception e) {
      logger.error("Unsuccessfully deleted events");
    }
  }
}
