package com.mile.nokenzivot.inject;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/inject")
class InjectionController {

  private final InjectionService injectionService;

  InjectionController(InjectionService injectionService) {
    this.injectionService = injectionService;
  }

  @PostMapping
  public CreateClubDTO createNewClub(@RequestBody CreateClubDTO createClubDTO) {
    return injectionService.createNewClub(createClubDTO);
  }

}
