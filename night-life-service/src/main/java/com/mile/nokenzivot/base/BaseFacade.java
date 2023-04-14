package com.mile.nokenzivot.base;

import com.mile.nokenzivot.global.dto.TypedCoordinates;
import com.mile.nokenzivot.global.entities.Club;
import com.mile.nokenzivot.global.entities.PartyEvent;
import org.springframework.stereotype.Service;
import java.sql.Date;
import java.util.Optional;
import java.util.Set;

@Service
public class BaseFacade {

  private final MainService mainService;

  public BaseFacade(MainService mainService) {
    this.mainService = mainService;
  }

  public Club getClubByEmail(String sender) {
   return mainService.getClubByEmail(sender);
  }

  public Optional<PartyEvent> getEventByDateAndClub(Date date, Club club) {
    return mainService.findEventByDateAndClub(date, club);
  }

  public Set<TypedCoordinates> getAllClubsCoordinates() {
    return mainService.getAllCoordinates();
  }

  public Club createNewClub(Club club) {
    return mainService.saveClub(club);
  }

  public void removeOutdatedEvents(Date date) {
    mainService.removeAllOutdatedEvents(date);
  }

  public Club getClubByName(String placeName) {
    return mainService.getClubByName(placeName);
  }
}
