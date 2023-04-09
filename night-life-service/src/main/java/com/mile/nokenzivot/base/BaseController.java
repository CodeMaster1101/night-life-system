package com.mile.nokenzivot.base;
import com.mile.nokenzivot.global.dto.Coordinates;
import org.springframework.web.bind.annotation.*;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/map/")
class BaseController {

  private final MainService mainService;

  BaseController(MainService mainService) {
    this.mainService = mainService;
  }

  @PostMapping("/place/on-click/{date}")
  public PartyEventDTO getClubAndEvent(
      @RequestBody Coordinates coordinates,
      @PathVariable("date") String date) {
    return mainService.getClubOnClick(coordinates, date);
  }

  @GetMapping("/filtered")
  public Set<Coordinates> filterPlacesByGenreAndPrice(
      @RequestParam(value = "genre", required = false) String genre,
      @RequestParam(value = "averagePrice", required = false) String averagePrice) {
    return mainService.filterPlacesByGenreAndPrice(genre, averagePrice);
  }

  @PostMapping("/on-hover")
  public OnHoverClub getClubOnHover(@RequestBody Coordinates coordinates) {
    return mainService.getClubOnHover(coordinates);
  }

  @GetMapping("/events/{date}")
  public Set<PartyEventDTO> getAllEvents(@PathVariable("date") String date) {
    return mainService.getAllEvents(date);
  }

}
