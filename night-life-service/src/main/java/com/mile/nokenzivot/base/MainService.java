package com.mile.nokenzivot.base;

import com.mile.nokenzivot.global.dto.Coordinates;
import com.mile.nokenzivot.global.dto.TypedCoordinates;
import com.mile.nokenzivot.global.entities.Club;
import com.mile.nokenzivot.global.entities.PartyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

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

  OnClickClub getClubOnClick(Coordinates coordinates, String date) {
    OnHoverClub onHoverClub = clubRepository.findByCoordinates(
        coordinates.getLatitude(),
        coordinates.getLongitude());
    return new OnClickClub(
        onHoverClub,
        partyEventRepository.findDtoByDateAndClubName(Date.valueOf(date), onHoverClub.getName()));
  }

  Set<Coordinates> filterPlaces(String genre, Integer averagePrice, String type) {
    try {
      return validateAndReturnFilteredInformation(genre, averagePrice, type);
    } catch (Exception e) {
      throw new NightLifeException("Something went wrong when fetching the filtered POIs, cause: ", e);
    }
  }

  private Set<Coordinates> validateAndReturnFilteredInformation(String genre, Integer averagePrice, String type) {
    if (genre == null && averagePrice == null && type == null) {
      return new HashSet<>();
    }
    Specification<Club> spec = (root, query, criteriaBuilder) ->
        getPredicate(genre, averagePrice, type, root, criteriaBuilder);
    return clubRepository.findAll(spec)
        .stream()
        .map(club -> new Coordinates(club.getLatitude(), club.getLongitude()))
        .collect(Collectors.toSet());
  }

  private Predicate getPredicate(String genre, Integer averagePrice, String type, Root<Club> root, CriteriaBuilder criteriaBuilder) {
    List<Predicate> predicates = new ArrayList<>();
    if (genre != null) {
      predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("genre")), genre.toLowerCase()));
    }
    if (averagePrice != null) {
      predicates.add(criteriaBuilder.equal(root.get("averageCost"), averagePrice));
    }
    if (type != null) {
      predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("type")), type.toLowerCase()));
    }
    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
  }

  OnHoverClub getClubOnHover(Coordinates coordinates) {
    return clubRepository.findByCoordinates(coordinates.getLatitude(),
        coordinates.getLongitude());
  }

  Set<PartyEventDTO> getAllEvents(String date) {
    return partyEventRepository.findAllByDate(Date.valueOf(date));
  }

  Set<TypedCoordinates> getAllCoordinates() {
    return clubRepository.getAllCoordinatesForClubs();
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
