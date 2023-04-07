package com.mile.nokenzivot.base;

import com.mile.nokenzivot.global.entities.Club;
import org.springframework.stereotype.Component;

@Component
class ClubMapper {

  private final PartyEventMapper partyEventMapper;

  ClubMapper(PartyEventMapper partyEventMapper) {
    this.partyEventMapper = partyEventMapper;
  }

  public ClubDTO convertToDTO(Club club) {
    return new ClubDTO(
        club.getRating(),
        partyEventMapper.convertToDTO(club.getEvent()));
  }
}
