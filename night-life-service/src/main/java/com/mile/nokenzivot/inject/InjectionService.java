package com.mile.nokenzivot.inject;

import com.mile.nokenzivot.base.BaseFacade;
import com.mile.nokenzivot.global.entities.Club;
import org.springframework.stereotype.Service;

@Service
class InjectionService {

  private final BaseFacade baseFacade;
  private final CreateClubMapper clubMapper;

  InjectionService(BaseFacade baseFacade, CreateClubMapper clubMapper) {
    this.baseFacade = baseFacade;
    this.clubMapper = clubMapper;
  }

  CreateClubDTO createNewClub(CreateClubDTO createClubDTO) {
    Club club = baseFacade.createNewClub(clubMapper.convertFromDTO(createClubDTO));
    return clubMapper.convertToDTO(club);
  }
}
