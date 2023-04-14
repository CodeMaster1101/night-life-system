package com.mile.nokenzivot.scrape_handler;

import com.mile.nokenzivot.base.BaseFacade;
import com.mile.nokenzivot.global.CustomPersistenceService;
import com.mile.nokenzivot.global.entities.Club;
import com.mile.nokenzivot.global.entities.PartyEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.Optional;

@Service
class ScrapedEventPersistenceService {

  private final BaseFacade baseFacade;
  private final CustomPersistenceService<PartyEvent> partyEventPersistenceService;

  ScrapedEventPersistenceService(BaseFacade baseFacade, CustomPersistenceService<PartyEvent> partyEventPersistenceService) {
    this.baseFacade = baseFacade;
    this.partyEventPersistenceService = partyEventPersistenceService;
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void updateEventInDB(ScrapedEvent event) {
    Club club = baseFacade.getClubByName(event.placeName());
    Date date = Date.valueOf(event.date());
    Optional<PartyEvent> dbEvent = baseFacade.getEventByDateAndClub(date, club);
    if (dbEvent.isPresent()) {
      partyEventPersistenceService.update(convertScrapedInformationToPartyEvent(event, dbEvent.get(), date));
    } else {
      PartyEvent partyEvent = new PartyEvent();
      partyEvent.setClub(club);
      partyEventPersistenceService.persist(convertScrapedInformationToPartyEvent(event, partyEvent, date));
    }
  }

  private PartyEvent convertScrapedInformationToPartyEvent(ScrapedEvent scrapedEvent, PartyEvent partyEvent, Date date) {
      partyEvent.setDescription(scrapedEvent.description());
      partyEvent.setName(scrapedEvent.subject());
      if (scrapedEvent.data() != null) {
        partyEvent.setThumbnail(scrapedEvent.data());
      }
      partyEvent.setDate(date);
      return partyEvent;
  }
}
