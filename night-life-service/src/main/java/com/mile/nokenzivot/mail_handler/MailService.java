package com.mile.nokenzivot.mail_handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
class MailService {

  private static final Logger logger = LoggerFactory.getLogger(MailService.class);
  private final HandleEventPersistence handleEventPersistence;

  MailService(HandleEventPersistence handleEventPersistence) {
    this.handleEventPersistence = handleEventPersistence;
  }

  void receiveMails(Set<MessageDTO> mails) {
    transformMailsIntoEvents(mails);
  }

  private void transformMailsIntoEvents(Set<MessageDTO> mails) {
    for (MessageDTO mail : mails) {
      try {
        handleEventPersistence.convertMailToEvent(mail);
        String successfulHandle = "Handled mail successfully, sender: " + mail.sender();
        logger.info(successfulHandle);
      } catch (Exception e) {
        String errorMsg = e.getCause() + "message: " + e.getMessage();
        logger.error(errorMsg);
      }
    }
  }
}
