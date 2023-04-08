package com.mile.mail.server.service;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
class EmailDelegatorService {

  @Value("${night-life-address}")
  private String nightLifeServerEndpoint;
  private final RestTemplate restTemplate;
  private static final Logger logger = LoggerFactory.getLogger(EmailDelegatorService.class);

  EmailDelegatorService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  void fetchEmails(List<Message> messages, Gmail gmail) {
    sendEmailInformationToServer(convertEmailMessagesToDtos(messages, gmail));
  }

  private void sendEmailInformationToServer(Set<MessageDTO> messages) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<Set<MessageDTO>> requestEntity = new HttpEntity<>(messages, headers);
    ResponseEntity<String> response = restTemplate.exchange(
        nightLifeServerEndpoint,
        HttpMethod.POST,
        requestEntity,
        String.class);
    String responseBody = response.getBody();
    logger.info(responseBody);
  }

  private Set<MessageDTO> convertEmailMessagesToDtos(List<Message> messages, Gmail gmail) {
    Set<MessageDTO> messagesToSendToServer = new HashSet<>();
    for (Message message : messages) {
      try {
        iterateMessages(message, gmail, messagesToSendToServer);
      } catch (Exception e) {
        String errorMsg = e.getMessage();
        logger.error(errorMsg);
      }
    }
    return messagesToSendToServer;
  }

  private void iterateMessages(Message message, Gmail gmail, Set<MessageDTO> messagesToSend) throws IOException {
      Message fullMessage = gmail.users().messages().get("me", message.getId()).execute();
      String sender = getHeaderValue(fullMessage, "From");
      String subject = getHeaderValue(fullMessage, "Subject");
      try {
        MessageDTO messageDTO = convertMailMessageToDTO(message, gmail, fullMessage, sender, subject);
        messagesToSend.add(messageDTO);
      } catch (Exception e) {
        throw new EmailFetchingException("could not convert email to dto", e);
      }

    }

  private MessageDTO convertMailMessageToDTO(Message message, Gmail gmail, Message fullMessage, String sender, String subject) throws IOException {
    List<MessagePart> parts = fullMessage.getPayload().getParts();
    String content = "";
    byte[] imageData = null;
    for (MessagePart part : parts) {
      String mimeType = part.getMimeType();
      if (mimeType.equals("multipart/alternative")) {
        List<MessagePart> subParts = part.getParts();
        for (MessagePart subPart : subParts) {
          String subMimeType = subPart.getMimeType();
          if (subMimeType.equals("text/plain") || subMimeType.equals("text/html")) {
            content = new String(Base64.getDecoder().decode(subPart.getBody().getData()));
            break;
          }
        }
      } else if (mimeType.startsWith("image/")) {
        imageData = processImage(message, gmail, part);
      }
    }
    return new MessageDTO(processSenderMessage(sender), subject, content, imageData);
  }

  private String processSenderMessage(String sender) {
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = sender.length() - 2; i > 0; i--) {
      if (sender.charAt(i) == '<') {
        break;
      }
      stringBuilder.append(sender.charAt(i));
    }
    return stringBuilder.reverse().toString();
  }

  private byte[] processImage(Message message, Gmail gmail, MessagePart part) throws IOException {
    String attachmentId = part.getBody().getAttachmentId();
    return gmail.users().messages().attachments().get("me", message.getId(), attachmentId).execute().decodeData();
  }

  private static String getHeaderValue(Message message, String name) {
    List<MessagePartHeader> headers = message.getPayload().getHeaders();
    for (MessagePartHeader header : headers) {
      if (name.equalsIgnoreCase(header.getName())) {
        return header.getValue();
      }
    }
    return null;
  }

}
