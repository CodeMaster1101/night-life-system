package com.mile.mail.server.service;

import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.mile.mail.server.service.GmailService.gmail;

@Service
public class EmailFetcherService {

  private final EmailDelegatorService emailDelegatorService;

  public EmailFetcherService(EmailDelegatorService emailDelegatorService) {
    this.emailDelegatorService = emailDelegatorService;
  }

  /*
  Fetches the emails every 10 minutes
   */
  @Scheduled(fixedRate = 600000)
  public void fetchUnreadEmails() {
    try {
      emailDelegatorService.fetchEmails(getUnreadMails(), gmail);
    } catch (IOException e) {
      throw new EmailFetchingException(e.getMessage(), e);
    }
  }
  private List<Message> getUnreadMails() throws IOException {
    ListMessagesResponse response = gmail.users().messages()
        .list("me")
        .setQ("is:unread")
        .execute();
    return response != null ? response.getMessages() : new ArrayList<>();
  }
}
