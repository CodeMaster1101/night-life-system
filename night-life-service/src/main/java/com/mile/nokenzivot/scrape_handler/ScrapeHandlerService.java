package com.mile.nokenzivot.scrape_handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
class ScrapeHandlerService {

  private static final Logger logger = LoggerFactory.getLogger(ScrapeHandlerService.class);
  private final ScrapedEventPersistenceService scrapedEventPersistenceService;

  ScrapeHandlerService(ScrapedEventPersistenceService service) {
    this.scrapedEventPersistenceService = service;
  }

  void updateEvents(Set<ScrapedEvent> eventInformation) {
    for (ScrapedEvent event : eventInformation) {
      try {
        scrapedEventPersistenceService.updateEventInDB(event);
        String successfulHandle = "Handled mail successfully, place: " + event.placeName();
        logger.info(successfulHandle);
      } catch (Exception e) {
        String errorMsg = e.getCause() + "message: " + e.getMessage();
        logger.error(errorMsg);
      }
    }
  }
}
