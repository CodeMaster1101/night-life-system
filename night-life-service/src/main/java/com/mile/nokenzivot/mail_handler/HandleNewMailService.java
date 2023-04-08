package com.mile.nokenzivot.mail_handler;

import com.mile.nokenzivot.base.BaseFacade;
import com.mile.nokenzivot.global.entities.Club;
import com.mile.nokenzivot.global.entities.PartyEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.Optional;

@Service
class HandleNewMailService {
  private final BaseFacade baseFacade;
  private final CustomPersistenceService<PartyEvent> partyEventPersistenceService;


  HandleNewMailService(
      BaseFacade baseFacade,
      CustomPersistenceService<PartyEvent> partyEventPersistenceService) {
    this.baseFacade = baseFacade;
    this.partyEventPersistenceService = partyEventPersistenceService;
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void convertMailToEvent(
      MessageDTO mail) {
    Club club = baseFacade.getClubByEmail(mail.sender());
    Optional<PartyEvent> event = baseFacade.getEventByDateAndClub(Date.valueOf(mail.date()), club);
    if (event.isPresent()) {
      partyEventPersistenceService.update(convertMailDTOToPartyEvent(mail, event.get()));
    } else {
      PartyEvent partyEvent = new PartyEvent();
      partyEvent.setClub(club);
      partyEventPersistenceService.persist(convertMailDTOToPartyEvent(mail, partyEvent));
    }
  }
  private PartyEvent convertMailDTOToPartyEvent(MessageDTO mail, PartyEvent partyEvent) {
    partyEvent.setDescription(mail.description());
    partyEvent.setName(mail.subject());
    if (mail.data() != null) {
      partyEvent.setThumbnail(mail.data());
    }
    return partyEvent;
  }
}
