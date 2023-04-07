package com.mile.nokenzivot.base;
import com.mile.nokenzivot.global.dto.Coordinates;
import com.mile.nokenzivot.global.entities.Club;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Set;

@Service
class MainService {

  private final ClubRepository clubRepository;
  private final ClubMapper clubMapper;

  MainService(ClubRepository clubRepository, ClubMapper clubMapper) {
    this.clubRepository = clubRepository;
    this.clubMapper = clubMapper;
  }

  ClubDTO getClubOnClick(Coordinates coordinates) {
      Club club = clubRepository.findByCoordinates(
          coordinates.latitude(),
          coordinates.longitude());
      return clubMapper.convertToDTO(club);
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

  public OnHoverClub getClubOnHover(Coordinates coordinates) {
    return clubRepository.findByCoordinatesHover(coordinates.latitude(), coordinates.longitude());
  }
}
