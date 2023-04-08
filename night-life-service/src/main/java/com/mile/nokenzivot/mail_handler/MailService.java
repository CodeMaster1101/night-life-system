package com.mile.nokenzivot.mail_handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.Set;

@Service
class MailService {

  private static final Logger logger = LoggerFactory.getLogger(MailService.class);
  private final HandleNewMailService handleNewMailService;

  MailService(HandleNewMailService handleNewMailService) {
    this.handleNewMailService = handleNewMailService;
  }

  void receiveMails(Set<MessageDTO> mails) {
    transformMailsIntoEvents(mails);
  }

  private void transformMailsIntoEvents(Set<MessageDTO> mails) {
    for (MessageDTO mail : mails) {
      try {
        handleNewMailService.convertMailToEvent(mail);
        String successfulHandle = "Handled mail successfully, sender: " + mail.sender();
        logger.info(successfulHandle);
      } catch (Exception e) {
        String errorMsg = e.getMessage();
        logger.error(errorMsg);
      }
    }
  }
}