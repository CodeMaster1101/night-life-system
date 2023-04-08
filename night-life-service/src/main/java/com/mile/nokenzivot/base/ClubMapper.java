package com.mile.nokenzivot.base;

import com.mile.nokenzivot.global.entities.Club;
import org.springframework.stereotype.Component;

@Component
class ClubMapper {

  public ClubDTO convertToDTO(Club club, PartyEventDTO partyEventDTO) {
    return new ClubDTO(
        club.getRating(),
        partyEventDTO);
  }
}
