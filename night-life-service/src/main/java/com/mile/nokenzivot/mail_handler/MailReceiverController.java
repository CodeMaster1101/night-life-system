package com.mile.nokenzivot.mail_handler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/mail")
class MailReceiverController {

  private final MailService mailService;

  MailReceiverController(MailService mailService) {
    this.mailService = mailService;
  }

  @PostMapping
  public ResponseEntity<String> receiveMailFromServer(@RequestBody Set<MessageDTO> mails) {
    if (mails == null || mails.isEmpty()) {
      return new ResponseEntity<>("Mails are not present or null", HttpStatus.BAD_REQUEST);
    } else {
       mailService.receiveMails(mails);
       return new ResponseEntity<>("successfully transformed the mails into new party events", HttpStatus.OK);

    }
  }

}
