package com.mile.nokenzivot.base;

import com.mile.nokenzivot.global.entities.PartyEvent;
import org.springframework.stereotype.Component;

@Component
class PartyEventMapper {

  public PartyEventDTO convertToDTO(PartyEvent partyEvent) {
    return new PartyEventDTO(
        partyEvent.getName(),
        partyEvent.getDescription(),
        partyEvent.getThumbnail(),
        partyEvent.getClub().getName());
  }

}
