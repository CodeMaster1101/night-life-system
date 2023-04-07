package com.mile.nokenzivot.start_up;

import com.mile.nokenzivot.base.BaseFacade;
import com.mile.nokenzivot.global.dto.Coordinates;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("api/v1/server-boot")
class StartUpController {

  private final BaseFacade baseFacade;

  StartUpController(BaseFacade baseFacade) {
    this.baseFacade = baseFacade;
  }

  @GetMapping("/coordinates")
  public Set<Coordinates> getCoordinates() {
    return baseFacade.getAllClubsCoordinates();
  }

}
